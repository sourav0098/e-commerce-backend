package com.quickpik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.Order;
import com.quickpik.entities.User;

public interface OrderRepository extends JpaRepository<Order, String> {
	List<Order> findByUser(User user);
}
