package com.techlab.ecommerce.order;

import java.util.LinkedHashMap;
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

    /**
     * Port of OrderService.createOrder from the simulator. All or nothing: every line is
     * checked before any stock is touched, and the whole method is one transaction, so if
     * anything fails halfway the database is left exactly as it was.
     */
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        User user = users.getEntity(request.userId());

        // The same product twice in the cart counts as one line with the summed quantity,
        // otherwise 2 lines of 3 units could each pass a stock check of 5
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        request.items().forEach(item -> quantities.merge(item.productId(), item.quantity(), Integer::sum));

        // Phase 1: validate everything
        Map<Product, Integer> toBuy = new LinkedHashMap<>();
        quantities.forEach((productId, quantity) -> {
            Product product = products.getEntity(productId);
            if (product.getStock() < quantity) {
                throw new BusinessRuleException("Not enough stock for '%s': available %d, requested %d"
                        .formatted(product.getName(), product.getStock(), quantity));
            }
            toBuy.put(product, quantity);
        });

        // Phase 2: only now change anything
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

    Order getEntity(Long id) {
        return orders.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
