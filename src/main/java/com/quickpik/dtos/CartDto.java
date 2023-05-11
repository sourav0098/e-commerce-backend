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

public class CartDto {
	private String cartId;
	private UserDto user;
	
	private List<CartItemDto> items = new ArrayList<>();

	private Date createdAt;
	private Date updatedAt;
}
