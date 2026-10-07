package com.techlab.ecommerce.order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.ecommerce.common.error.BusinessRuleException;
import com.techlab.ecommerce.common.error.ResourceNotFoundException;
import com.techlab.ecommerce.product.Product;
import com.techlab.ecommerce.product.ProductService;
import com.techlab.ecommerce.user.User;
import com.techlab.ecommerce.user.UserService;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orders;
    private final ProductService products;
    private final UserService users;

    public OrderService(OrderRepository orders, ProductService products, UserService users) {
        this.orders = orders;
        this.products = products;
        this.users = users;
    }

    /** All or nothing: every line is checked before any stock is touched. */
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        User user = users.getEntity(request.userId());

        // same product twice in the cart: check the summed quantity, not each line
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        request.items().forEach(item -> quantities.merge(item.productId(), item.quantity(), Integer::sum));

        // validate everything first
        Map<Product, Integer> toBuy = new LinkedHashMap<>();
        quantities.forEach((productId, quantity) -> {
            Product product = products.getEntity(productId);
            if (product.getStock() < quantity) {
                throw new BusinessRuleException("Not enough stock for '%s': available %d, requested %d"
                        .formatted(product.getName(), product.getStock(), quantity));
            }
            toBuy.put(product, quantity);
        });

        // then deduct
        Order order = new Order(user);
        toBuy.forEach((product, quantity) -> {
            product.setStock(product.getStock() - quantity);
            order.addLine(new OrderLine(product, quantity));
        });

        return OrderResponse.from(orders.save(order));
    }

    public OrderResponse get(Long id) {
        return OrderResponse.from(getEntity(id));
    }

    public List<OrderResponse> history(Long userId) {
        users.getEntity(userId); // 404, not an empty list
        return orders.findByUser_IdOrderByCreatedAtDesc(userId).stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus next) {
        Order order = getEntity(id);
        OrderStatus current = order.getStatus();
        if (!current.canMoveTo(next)) {
            throw new BusinessRuleException("An order can't go from %s to %s".formatted(current, next));
        }

        if (next == OrderStatus.CANCELLED) {
            order.getLines().forEach(line -> {
                Product product = line.getProduct();
                product.setStock(product.getStock() + line.getQuantity());
            });
        }

        order.setStatus(next);
        return OrderResponse.from(order);
    }

    Order getEntity(Long id) {
        return orders.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
