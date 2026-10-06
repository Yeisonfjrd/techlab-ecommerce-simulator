package com.techlab.ecommerce.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.techlab.ecommerce.category.Category;
import com.techlab.ecommerce.category.CategoryRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository products;

    @Autowired
    private CategoryRepository categories;

    @Autowired
    private EntityManager em;

    @Test
    void savesAProductWithItsCategoryAndKeepsTheExactPrice() {
        Category peripherals = categories.save(new Category("Periféricos"));
        Product mouse = products.save(new Product("Mouse", "Inalámbrico", new BigDecimal("19.99"), 10, null, peripherals));

        // Clear the persistence context so the next read really goes to the database
        em.flush();
        em.clear();

        Product found = products.findById(mouse.getId()).orElseThrow();
        assertThat(found.getPrice()).isEqualByComparingTo("19.99");
        assertThat(found.getCategory().getName()).isEqualTo("Periféricos");
    }

    @Test
    void searchesByNameIgnoringCase() {
        products.save(new Product("Teclado mecánico", null, new BigDecimal("50.00"), 3, null, null));
        products.save(new Product("Monitor", null, new BigDecimal("200.00"), 1, null, null));

        assertThat(products.findByNameContainingIgnoreCase("TECLADO"))
                .extracting(Product::getName)
                .containsExactly("Teclado mecánico");
    }
}
