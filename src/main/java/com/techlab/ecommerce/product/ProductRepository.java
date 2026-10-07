package com.techlab.ecommerce.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByStockLessThanEqualOrderByStockAsc(int threshold);

    @Query("select count(l) > 0 from OrderLine l where l.product.id = :productId")
    boolean isInAnyOrder(Long productId);
}
