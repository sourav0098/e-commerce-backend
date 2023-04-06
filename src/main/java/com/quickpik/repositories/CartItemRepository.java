package com.quickpik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickpik.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
