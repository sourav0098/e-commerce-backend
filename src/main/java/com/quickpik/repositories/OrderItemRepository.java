package com.quickpik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	
}