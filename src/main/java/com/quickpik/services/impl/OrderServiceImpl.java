package com.quickpik.services.impl;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.quickpik.dtos.CreateOrderRequest;
import com.quickpik.dtos.OrderDto;
import com.quickpik.dtos.PageableResponse;
import com.quickpik.entities.Cart;
import com.quickpik.entities.CartItem;
import com.quickpik.entities.Order;
import com.quickpik.entities.OrderItem;
import com.quickpik.entities.User;
import com.quickpik.exception.BadApiRequestException;
import com.quickpik.exception.ResourceNotFoundException;
import com.quickpik.repositories.CartRepository;
import com.quickpik.repositories.OrderRepository;
import com.quickpik.repositories.UserRepository;
import com.quickpik.services.OrderService;

public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderDto> getOrdersByUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDto createOrder(CreateOrderRequest orderRequest) {
		// Get user from the database based on userId
		User user = this.userRepository.findById(orderRequest.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

		// Get cart from the database based on cartId
		Cart cart = this.cartRepository.findById(orderRequest.getCartId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

		// Get items from the cart
		List<CartItem> cartItems = cart.getItems();

		// If there are no items in the cart, throw an exception
		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("No items found in cart");
		}

		// Generate a unique orderId and orderNumber
		String orderId = UUID.randomUUID().toString();
		String orderNumber = "ORD-" + System.currentTimeMillis() / 1000L + "-" + new Random().nextInt(1000);
		AtomicReference<Double> totalOrderAmount = new AtomicReference<Double>((double) 0);

		Order order = Order.builder().orderId(orderId).orderNumber(orderNumber)
				.billingName(orderRequest.getBillingName()).billingPhone(orderRequest.getBillingPhone())
				.orderStatus(orderRequest.getOrderStatus()).paymentStatus(orderRequest.getPaymentStatus())
				.orderAddress(orderRequest.getOrderAddress()).city(orderRequest.getCity())
				.province(orderRequest.getProvince()).postalCode(orderRequest.getPostalCode()).user(user).build();

		// order items, amount to be set

		// convert cart items into order items
		List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			// cart item -> order item
			OrderItem orderItem = OrderItem.builder().quantity(cartItem.getQuantity()).product(cartItem.getProduct())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice()).order(order)
					.build();

			// Update the totalOrderAmount by adding the price of the OrderItem
			totalOrderAmount.set(totalOrderAmount.get() + orderItem.getTotalPrice());

			return orderItem;
		}).collect(Collectors.toList());

		// set order items and order amount
		order.setOrderItems(orderItems);
		order.setOrderAmount(totalOrderAmount.get());

		// Clear cart after order is made
		cart.getItems().clear();
		this.cartRepository.save(cart);
		Order savedOrder = this.orderRepository.save(order);

		return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		// TODO Auto-generated method stub

	}

}
