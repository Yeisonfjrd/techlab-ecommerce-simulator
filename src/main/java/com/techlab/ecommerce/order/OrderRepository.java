package com.techlab.ecommerce.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // "User_Id" walks the relationship: WHERE o.user.id = ?
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
