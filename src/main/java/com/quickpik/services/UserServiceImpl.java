package com.quickpik.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quickpik.dtos.UserDto;
import com.quickpik.entities.User;
import com.quickpik.exception.ResourceNotFoundException;
import com.quickpik.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto getUserById(String userId) {
		User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found!"));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("No user found!"));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepository.findAll();
		List<UserDto> usersDto = users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return usersDto;
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = this.userRepository.findByFnameContaining(keyword);
		List<UserDto> usersDto = users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return usersDto;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		// Generate UUID
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);

		// Convert DTO to entity
		User user = modelMapper.map(userDto, User.class);
		User savedUser = this.userRepository.save(user);

		// Convert entity to DTO
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
		user.setFname(userDto.getFname());
		user.setLname(userDto.getLname());
		user.setPassword(userDto.getPassword());
		user.setAddress(userDto.getAddress());
		user.setCity(userDto.getCity());
		user.setProvince(userDto.getProvince());
		user.setPostalCode(userDto.getPostalCode());
		user.setPhone(userDto.getPhone());
		user.setImage(userDto.getImage());
		User savedUser = this.userRepository.save(user);

		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) {
		this.userRepository.deleteById(userId);
	}
}
