package com.quickpik.services;

import java.util.List;
import com.quickpik.dtos.CreateOrderRequest;
import com.quickpik.dtos.OrderDto;
import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UpdateOrderRequest;

public interface OrderService {

	// get all orders(for admin)
	PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

	OrderDto getOrderById(String orderId);
	
	// get order for user
	List<OrderDto> getOrdersByUser(String userId);

	// create order
	OrderDto createOrder(CreateOrderRequest orderRequest);

	// update order
	OrderDto updateOrder(String orderId, UpdateOrderRequest orderRequest);

	// remove order
	void removeOrder(String orderId);
}