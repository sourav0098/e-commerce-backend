package com.quickpik.services;

import java.io.IOException;
import java.util.List;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UserDto;

public interface UserService {
	UserDto getUserById(String userId);
	UserDto getUserByEmail(String email);
	PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
	List<UserDto> searchUser(String keyword);
	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, String userId);
	void deleteUser(String userId) throws IOException;
}
