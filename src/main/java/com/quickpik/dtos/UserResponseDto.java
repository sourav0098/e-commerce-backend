package com.quickpik.dtos;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import com.quickpik.entities.AuthenticationProvider;

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
public class UserResponseDto {
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

	@Size(min = 10, max = 10, message = "Please provide a valid 10 digit phone number")
	private String phone;

	private String address;

	@Pattern(regexp = "^(?!.*[DFIOQUdfioqu])[A-VXYa-vxy][0-9][A-Za-z] ?[0-9][A-Za-z][0-9]?|\\s*$", message = "Please enter a valid postal code")
	private String postalCode;

	@Size(max = 150, message = "City name cant be longer than 150 characters")
	private String city;
	@Size(max = 100, message = "Province name cant be longer than 100 characters")
	private String province;

	// Custom validator annotation
//	@ImageNameValid
	private String image;

	private AuthenticationProvider authenticationProvider;

	private Set<RoleDto> roles = new HashSet<>();

	private Date createdAt;
	private Date updatedAt;
}
