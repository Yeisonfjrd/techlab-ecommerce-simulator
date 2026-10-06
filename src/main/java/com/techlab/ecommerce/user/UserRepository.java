package com.techlab.ecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("select count(o) > 0 from Order o where o.user.id = :userId")
    boolean hasOrders(Long userId);
}
