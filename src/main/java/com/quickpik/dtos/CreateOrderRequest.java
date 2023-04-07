package com.quickpik.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quickpik.entities.Order;
import com.quickpik.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("cart_id")
	private String cartId;

	@JsonProperty("order_status")
	private String orderStatus = "PENDING";

	@JsonProperty("payment_status")
	private String paymentStatus = "NOT-PAID";

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
}
