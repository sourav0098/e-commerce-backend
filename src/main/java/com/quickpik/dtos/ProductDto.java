package com.quickpik.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@JsonPropertyOrder({ "product_id", "brand", "title", "description", "unit_price", "discounted_price",
		"quantity", "product_image", "is_stock", "is_live", "created_at", "updated_at", "category" })
public class ProductDto {
	@JsonProperty(value = "product_id")
	private String productId;
	
	private CategoryDto category;

	@NotBlank(message = "Please provide a valid brand name")
	private String brand;
	@NotBlank(message = "Please provide a valid title")
	private String title;

	@NotBlank(message = "Please provide a valid descripiton")
	private String description;

	@JsonProperty(value = "unit_price")
	@Min(value = 0, message = "Unit Price must be greater than 0")
	@Digits(integer = 10, fraction = 2, message = "Unit Price should be only upto 2 decimal places")
	private double unitPrice;

	@JsonProperty(value = "discounted_price")
	@Min(value = 0, message = "Discounted Price must be greater than or equal to 0")
	@Digits(integer = 10, fraction = 2, message = "Discounted Price should be only upto 2 decimal places")
	private double discountedPrice;

	@Min(value = 0, message = "Quantity must be greater than or equal to 0")
	@Digits(integer = 10, fraction = 0, message = "Quantity should be a whole number")
	private int quantity;

	@JsonProperty(value = "product_image")
	private String productImage;

	@JsonProperty(value = "is_live")
	private boolean isLive;

	@JsonProperty(value = "is_stock")
	private boolean isStock;

	@JsonProperty(value = "created_at")
	private Date createdAt;

	@JsonProperty(value = "updated_at")
	private Date updatedAt;
}