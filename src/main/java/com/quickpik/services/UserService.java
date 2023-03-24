package com.quickpik.services;

import java.util.List;

import com.quickpik.dtos.UserDto;

public interface UserService {
	UserDto getUserById(String userId);
	UserDto getUserByEmail(String email);
	List<UserDto> getAllUsers();
	List<UserDto> searchUser(String keyword);
	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, String userId);
	void deleteUser(String userId);
}
