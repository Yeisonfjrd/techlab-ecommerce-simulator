package com.techlab.ecommerce.product;

import java.math.BigDecimal;

import com.techlab.ecommerce.category.Category;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stock,
        String imageUrl,
        Long categoryId,
        String categoryName) {

    static ProductResponse from(Product product) {
        Category category = product.getCategory();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                category == null ? null : category.getId(),
                category == null ? null : category.getName());
    }
}
