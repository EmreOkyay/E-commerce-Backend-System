package com.backend.Ecommerce.order;

import com.backend.Ecommerce.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(AppUser user);
}
