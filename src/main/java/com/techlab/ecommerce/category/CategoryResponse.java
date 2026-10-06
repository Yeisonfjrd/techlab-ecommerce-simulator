package com.techlab.ecommerce.category;

public record CategoryResponse(Long id, String name) {

    static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
