package com.quickpik.dtos;

import java.util.Date;
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
	private String orderId;
	private String orderStatus;
	private String paymentStatus;
	@NotBlank(message = "Please provide a billing name")
	private String shippingName;

	@NotBlank(message = "Please provide a valid address")
	private String shippingAddress;

	@NotBlank(message = "Please provide a postal code")
	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$", message = "Please enter valid zip code")
	private String postalCode;

	@NotBlank(message = "Please provide a city")
	private String city;

	@NotBlank(message = "Please provide a province")
	private String province;

	@NotBlank(message = "Please provide a billing phone number")
	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	private String shippingPhone;

	private Date deliveredDate;
}