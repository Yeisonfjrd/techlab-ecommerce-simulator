package com.techlab.ecommerce.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Plain unit test: no Spring, runs in milliseconds. */
class OrderStatusTest {

    @ParameterizedTest
    @CsvSource({
            "PENDING, PAID",
            "PENDING, CANCELLED",
            "PAID, SHIPPED",
            "PAID, CANCELLED",
            "SHIPPED, DELIVERED"})
    void allowedTransitions(OrderStatus from, OrderStatus to) {
        assertThat(from.canMoveTo(to)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "PENDING, SHIPPED",     // can't skip payment
            "SHIPPED, CANCELLED",   // already left the warehouse
            "DELIVERED, PENDING",   // no going back
            "CANCELLED, PAID",
            "PAID, PAID"})
    void forbiddenTransitions(OrderStatus from, OrderStatus to) {
        assertThat(from.canMoveTo(to)).isFalse();
    }

    @Test
    void finalStatesGoNowhere() {
        for (OrderStatus next : OrderStatus.values()) {
            assertThat(OrderStatus.DELIVERED.canMoveTo(next)).isFalse();
            assertThat(OrderStatus.CANCELLED.canMoveTo(next)).isFalse();
        }
    }
}
