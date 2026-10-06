package com.techlab.ecommerce.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Same search as ProductService.getProductByName in the simulator
    List<Product> findByNameContainingIgnoreCase(String name);

    // JPQL works on entities, so this package doesn't need to import the order classes
    @Query("select count(l) > 0 from OrderLine l where l.product.id = :productId")
    boolean isInAnyOrder(Long productId);
}
