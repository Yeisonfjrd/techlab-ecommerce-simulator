package com.techlab.ecommerce.user;

public record UserResponse(Long id, String name, String email, Role role) {

    static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
