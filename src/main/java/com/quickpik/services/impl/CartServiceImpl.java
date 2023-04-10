package com.quickpik.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quickpik.dtos.AddItemToCartRequest;
import com.quickpik.dtos.CartDto;
import com.quickpik.entities.Cart;
import com.quickpik.entities.CartItem;
import com.quickpik.entities.Product;
import com.quickpik.entities.User;
import com.quickpik.exception.ResourceNotFoundException;
import com.quickpik.repositories.CartItemRepository;
import com.quickpik.repositories.CartRepository;
import com.quickpik.repositories.ProductRepository;
import com.quickpik.repositories.UserRepository;
import com.quickpik.services.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CartDto getCartByUser(String userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found"));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found"));
		return modelMapper.map(cart, CartDto.class);
	}

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		int quantity = request.getQuantity();
		String productId = request.getProductId();

		// fetch product
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("No product found"));

		// fetch user
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found"));

		Cart cart = null;

		try {
			// fetch cart for user
			cart = this.cartRepository.findByUser(user).get();
		} catch (NoSuchElementException ex) {
			// if no cart found then create cart
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
		}

		// PERFORM CART OPERATIONS
		AtomicReference<Boolean> updated = new AtomicReference<>(false);

		// get all items for cart
		List<CartItem> items = cart.getItems();

		// if cart item already present update quantity and price if required
		items = items.stream().map(item -> {
			if (item.getProduct().getProductId().equals(productId)) {
				// item already present in cart update quantity, price and set upated flag to
				// true
				item.setQuantity(quantity);
				item.setTotalPrice((product.getDiscountedPrice() == 0) ? quantity * product.getUnitPrice()
						: quantity * product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());


		// In case previous item is updated then we dont need to create new item
		if (!updated.get()) {
			// create cart item
			CartItem cartItem = CartItem.builder().quantity(quantity)
					.totalPrice((product.getDiscountedPrice() == 0) ? quantity * product.getUnitPrice()
							: quantity * product.getDiscountedPrice())
					.product(product).cart(cart).build();

			cart.getItems().add(cartItem);
			cart.setUser(user);
		}

		// Update the cart in database
		Cart updatedCart = cartRepository.save(cart);
		return modelMapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
		// check if user cart has that item or not

		CartItem cartItem = this.cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found"));
		this.cartItemRepository.delete(cartItem);
	}

	@Override
	public void clearCart(String userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found"));
		Cart cart = this.cartRepository.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("No cart found"));
		cart.getItems().clear();
		this.cartRepository.save(cart);
	}
}