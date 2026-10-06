package com.techlab.ecommerce.order;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lives in the order package, not in UserController, so the user module
 * doesn't depend on orders. Orders depend on users, not the other way round.
 */
@RestController
public class UserOrdersController {

    private final OrderService service;

    public UserOrdersController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/api/users/{userId}/orders")
    public List<OrderResponse> history(@PathVariable Long userId) {
        return service.history(userId);
    }
}
