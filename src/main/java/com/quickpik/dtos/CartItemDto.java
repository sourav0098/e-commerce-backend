package com.quickpik.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class CartItemDto {
	private int cartItemId;

	@NotNull(message = "Product cannot be null")
	private ProductDto product;

	@Min(value = 1, message = "Quantity must be positive")
	private int quantity;

	@DecimalMin(value = "0.0", inclusive = false, message = "Total price must be positive")
	private double totalPrice;
}