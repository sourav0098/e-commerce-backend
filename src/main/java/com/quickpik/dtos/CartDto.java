package com.quickpik.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@JsonPropertyOrder({ "cart_id", "user_id", "items", "created_at", "updated_at" })
public class CartDto {
	@JsonProperty(value = "cart_id")
	private String cartId;

	@JsonProperty(value = "user_id")
	private String userId;

	private List<CartItemDto> items = new ArrayList<>();

	@JsonProperty(value = "created_at")
	private Date createdAt;

	@JsonProperty(value = "updated_at")
	private Date updatedAt;
}
