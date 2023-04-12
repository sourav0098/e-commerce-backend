package com.quickpik.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	private String billingName;
	private String orderAddress;
	private String postalCode;
	private String city;
	private String province;
	private String billingPhone;
	private Date deliveredDate;
	private Date createdAt;

	private List<OrderItemDto> orderItems = new ArrayList<>();
}
