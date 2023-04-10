package com.quickpik.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UpdateOrderRequest {
	@JsonProperty("order_id")
	private String orderId;

	@JsonProperty("order_status")
	private String orderStatus;

	@JsonProperty("payment_status")
	private String paymentStatus;

	@NotBlank(message = "Please provide a billing name")
	@JsonProperty("billing_name")
	private String billingName;

	@NotBlank(message = "Please provide a valid address")
	@JsonProperty("order_address")
	private String orderAddress;

	@NotBlank(message = "Please provide a postal code")
	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$", message = "Please enter valid zip code")
	@JsonProperty("postal_code")
	private String postalCode;

	@NotBlank(message = "Please provide a city")
	private String city;

	@NotBlank(message = "Please provide a province")
	private String province;

	@NotBlank(message = "Please provide a billing phone number")
	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	@JsonProperty("billing_phone")
	private String billingPhone;

	private Date deliveredDate;
}
