package com.quickpik.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	@JsonProperty("user_id")
	private String userId;

	@Size(max = 100, message = "First name cant be longer than 100 characters")
	@NotBlank(message = "Please provide a first name")
	private String fname;

	@Size(max = 100, message = "Last name cant be longer than 100 characters")
	@NotBlank(message = "Please provide a last name")
	private String lname;

	@Email(message = "Please provide a valid email")
	@NotBlank(message = "Please provide an email")
	private String email;

	@NotBlank(message = "Please provide a password")
	private String password;

	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	private String phone;

	private String address;

	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]$", message = "Please enter valid zip code")
	@JsonProperty("postal_code")
	private String postalCode;

	@Size(max = 150, message = "City name cant be longer than 150 characters")
	private String city;
	@Size(max = 100, message = "Province name cant be longer than 100 characters")
	private String province;
	private String image;
	
	@JsonProperty("created_at")
	private Date createdAt;
	
	@JsonProperty("updated_at")
	private Date updatedAt;
}