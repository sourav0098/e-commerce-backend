package com.quickpik.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.Cart;
import com.quickpik.entities.User;

public interface CartRepository extends JpaRepository<Cart, String>{
	Optional<Cart> findByUser(User user);
}
