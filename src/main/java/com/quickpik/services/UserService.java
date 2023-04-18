package com.quickpik.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UserDto;
import com.quickpik.entities.User;

public interface UserService {

	// Get User By user Id
	UserDto getUserById(String userId);

	// Get User By user email
	UserDto getUserByEmail(String email);

	// Returns a pageable response containing all users, sorted and filtered
	// according to the specified parameters
	PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

	// Searches for users with the specified keyword, returning a list of user DTOs
	List<UserDto> searchUser(String keyword);

	// Create a new user
	UserDto createUser(UserDto userDto);

	// Update a user
	UserDto updateUser(UserDto userDto, String userId);

	// Delete a user along with profile image
	void deleteUser(String userId) throws IOException;
	
	Optional<User> findUserByEmailOptional(String email);
}