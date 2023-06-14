package com.quickpik.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.quickpik.dtos.CreateOrderRequest;
import com.quickpik.dtos.OrderDto;
import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UpdateOrderRequest;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// create order
	@PostMapping()
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
		OrderDto orderDto = this.orderService.createOrder(request);
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
	}

	// update order
	@PutMapping("/{orderId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId,
			@Valid @RequestBody UpdateOrderRequest request) {
		System.out.println(request.getDeliveredDate());
		OrderDto orderDto = this.orderService.updateOrder(orderId, request);
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
	}

	// remove order
	@DeleteMapping("/{orderId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId) {
		this.orderService.removeOrder(orderId);
		ApiResponse response = ApiResponse.builder().message("Order removed successfully").status(HttpStatus.OK.value())
				.timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}

	// get all orders

	@GetMapping()
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		PageableResponse<OrderDto> orders = this.orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<OrderDto>>(orders, HttpStatus.OK);
	}

	// get order by order id
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable String orderId) {
		OrderDto orderDto = this.orderService.getOrderById(orderId);
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
	}

	// get order by user
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable String userId) {
		List<OrderDto> orders = this.orderService.getOrdersByUser(userId);
		return new ResponseEntity<List<OrderDto>>(orders, HttpStatus.OK);
	}
}