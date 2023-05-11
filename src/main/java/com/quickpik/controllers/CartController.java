package com.quickpik.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quickpik.dtos.AddItemToCartRequest;
import com.quickpik.dtos.CartDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.CartService;


@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartService cartService;

	// get cart for a user
	@GetMapping("/user/{userId}")
	public ResponseEntity<CartDto> getCartForUser(@PathVariable String userId) {
		CartDto cartDto = this.cartService.getCartByUser(userId);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

	// add items to user cart
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
			@Valid @RequestBody AddItemToCartRequest request) {
		CartDto cartDto = this.cartService.addItemToCart(userId, request);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

	// remove item from cart
	@DeleteMapping("/{userId}/item/{itemId}")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId) {
		this.cartService.removeItemFromCart(userId, itemId);
		ApiResponse response = ApiResponse.builder().message("Item removed successfully").status(HttpStatus.OK.value())
				.timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	// delete all items from cart
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {
		this.cartService.clearCart(userId);
		ApiResponse response = ApiResponse.builder().message("Cart cleared successfully").status(HttpStatus.OK.value())
				.timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
}