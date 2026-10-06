package com.techlab.ecommerce.order;

import java.util.Set;

/**
 * The simulator stored free strings and let any status become any other.
 * Here each status knows which ones can follow it:
 *
 * PENDING -> PAID -> SHIPPED -> DELIVERED
 *    \         \
 *     ------------> CANCELLED
 */
public enum OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canMoveTo(OrderStatus next) {
        return allowedNext().contains(next);
    }

    private Set<OrderStatus> allowedNext() {
        return switch (this) {
            case PENDING -> Set.of(PAID, CANCELLED);
            case PAID -> Set.of(SHIPPED, CANCELLED);
            case SHIPPED -> Set.of(DELIVERED);
            case DELIVERED, CANCELLED -> Set.of();
        };
    }
}
