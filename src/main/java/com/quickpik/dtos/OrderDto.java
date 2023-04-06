package com.quickpik.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("order_number")
	private String orderNumber;

	@JsonProperty("order_status")
	private String orderStatus;

	@JsonProperty("payment_status")
	private String paymentStatus;

	@JsonProperty("order_amount")
	private double orderAmount;

	@JsonProperty("billing_name")
	private String billingName;

	@JsonProperty("order_address")
	private String orderAddress;

	@JsonProperty("postal_code")
	private String postalCode;
	private String city;
	private String province;

	@JsonProperty("billing_phone")
	private String billingPhone;

	@JsonProperty("delivered_date")
	private Date deliveredDate;

	@JsonProperty("created_at")
	private Date createdAt;

	private List<OrderItemDto> orderItemsDto = new ArrayList<>();
}
