package com.quickpik.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
	private String orderId;

	@NotBlank(message = "Please provide a order name")
	private String orderName;

	@NotBlank(message = "Please provide a valid address")
	private String shippingAddress;

	@NotBlank(message = "Please provide a postal code")
	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$", message = "Please enter valid zip code")
	private String postalCode;

	@NotBlank(message = "Please provide a city")
	private String city;

	@NotBlank(message = "Please provide a province")
	private String province;

	@NotBlank(message = "Please provide a shipping phone number")
	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	private String shippingPhone;

	@NotBlank(message = "Please provide a order status")
	private String orderStatus;
	
	@NotBlank(message = "Please provide a payment status")
	private String paymentStatus;
	
	
	private LocalDate deliveredDate;
}