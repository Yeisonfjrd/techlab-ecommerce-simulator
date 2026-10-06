package com.techlab.ecommerce.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Same search as ProductService.getProductByName in the simulator
    List<Product> findByNameContainingIgnoreCase(String name);
}
