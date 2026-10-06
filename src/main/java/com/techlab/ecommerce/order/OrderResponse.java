package com.techlab.ecommerce.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        OrderStatus status,
        BigDecimal total,
        Instant createdAt,
        List<Line> lines) {

    public record Line(Long productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    static OrderResponse from(Order order) {
        List<Line> lines = order.getLines().stream()
                .map(l -> new Line(l.getProduct().getId(), l.getProductName(), l.getQuantity(), l.getUnitPrice(), l.getLineTotal()))
                .toList();
        return new OrderResponse(order.getId(), order.getUser().getId(), order.getStatus(), order.getTotal(), order.getCreatedAt(), lines);
    }
}
