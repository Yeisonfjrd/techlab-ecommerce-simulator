package com.techlab.ecommerce.order;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // fetch lines in the same query (N+1 otherwise)
    @EntityGraph(attributePaths = "lines")
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
