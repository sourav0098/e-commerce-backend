package com.quickpik.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto {
	private String orderId;
	private String orderNumber;
	private String orderStatus;
	private String paymentStatus;
	private double orderAmount;
	
	@NotBlank(message="Please provide a valid name")
	private String shippingName;
	
	@NotBlank(message="Please provide a shipping address")
	private String shippingAddress;
	
	@NotBlank(message="Please provide a postal code")
	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]?|\\s*$", message = "Please enter a valid postal code")
	private String postalCode;
	
	@NotBlank(message="Please provide a order city")
	private String city;
	
	@NotBlank(message="Please provide a province")
	private String province;
	
	@NotBlank(message="Please provide a phone number")
	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	private String shippingPhone;
	private Date deliveredDate;
	private Date createdAt;

	private List<OrderItemDto> orderItems = new ArrayList<>();
}
