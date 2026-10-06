package com.techlab.ecommerce.order;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // "User_Id" walks the relationship: WHERE o.user.id = ?
    // The entity graph loads the lines in the same query; without it, listing
    // 20 orders would run 1 query for the orders + 20 more for their lines (N+1)
    @EntityGraph(attributePaths = "lines")
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
