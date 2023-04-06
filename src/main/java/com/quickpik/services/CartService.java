package com.quickpik.services;

import com.quickpik.dtos.AddItemToCartRequest;
import com.quickpik.dtos.CartDto;

public interface CartService {
	// Case 1: Cart not available for user: Create a cart for user and add items in
	// cart
	// Case 2: Cart available: Add items in cart

	// fetch cart by user
	CartDto getCartByUser(String userId);

	// Add items in cart
	CartDto addItemToCart(String userId, AddItemToCartRequest request);

	// Remove item from cart
	void removeItemFromCart(String userId, int cartItemId);

	// remove all items from cart
	void clearCart(String userId);
}