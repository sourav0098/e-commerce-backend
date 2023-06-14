package com.quickpik.services;

import java.util.Optional;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UserDto;
import com.quickpik.dtos.UserResponseDto;
import com.quickpik.entities.User;

public interface UserService {

	// Get User By user Id
	UserDto getUserById(String userId);

	// Get User By user email
	UserResponseDto getUserByEmail(String email);

	// Returns a pageable response containing all users, sorted and filtered
	// according to the specified parameters
	PageableResponse<UserResponseDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Searches for users with the specified keyword, returning a list of user DTOs
	PageableResponse<UserResponseDto> searchUser(String keyword, int pageNumber, int pageSize, String sortBy,
			String sortDir);

	// Create a new user
	UserDto createUser(UserDto userDto);

	// Update a user
	UserDto updateUser(UserDto userDto, String userId);

	Optional<User> findUserByEmailOptional(String email);
}