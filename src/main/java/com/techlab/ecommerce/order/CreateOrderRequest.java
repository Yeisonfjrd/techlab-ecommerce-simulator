package com.techlab.ecommerce.order;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** The cart: who is buying and what. An empty cart is rejected, like in the simulator. */
public record CreateOrderRequest(
        @NotNull Long userId,
        @NotEmpty List<@Valid Item> items) {

    public record Item(@NotNull Long productId, @NotNull @Positive Integer quantity) {
    }
}
