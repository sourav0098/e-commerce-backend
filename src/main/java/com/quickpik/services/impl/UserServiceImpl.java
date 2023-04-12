package com.quickpik.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UserDto;
import com.quickpik.entities.User;
import com.quickpik.exception.ResourceNotFoundException;
import com.quickpik.helper.Helper;
import com.quickpik.repositories.UserRepository;
import com.quickpik.services.UserService;

/**
 * This class implements the UserService interface and provides the business
 * logic for user-related operations.
 */

@Service
public class UserServiceImpl implements UserService {

	@Value("${user-image-path}")
	private String imagePath;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Retrieve a user by their ID from the database.
	@Override
	public UserDto getUserById(String userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No user found!"));
		return modelMapper.map(user, UserDto.class);
	}

	// Retrieve a user by their email address from the database.
	@Override
	public UserDto getUserByEmail(String email) {
		User user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("No user found!"));
		return modelMapper.map(user, UserDto.class);
	}

	/**
	 * This method retrieves all users from the database in a pageable format,
	 * sorted according to the given parameters.
	 */

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

		// Sorting
		// Create a Spring Data Sort object based on the given sortBy and sortDir
		// parameters.
		Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

		// Paging
		// Create a Spring Data Pageable object based on the given pageNumber, pageSize,
		// and sort parameters.
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		// Query the database for the page of User objects.
		Page<User> page = this.userRepository.findAll(pageable);

		// Convert the page of User objects into a PageableResponse of UserDto objects
		// using a helper method.
		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
		return response;
	}

	// Search for users in the database whose first name contains the specified
	// keyword.
	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = this.userRepository.findByFnameContaining(keyword);
		List<UserDto> usersDto = users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return usersDto;
	}

	// Create a new User
	@Override
	public UserDto createUser(UserDto userDto) {
		// Generate UUID
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);

		// Encoding password
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

		// Convert DTO to entity
		User user = modelMapper.map(userDto, User.class);
		User savedUser = this.userRepository.save(user);

		// Convert entity to DTO
		return modelMapper.map(savedUser, UserDto.class);
	}

	// Update a user
	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
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

	// Delete a user
	@Override
	public void deleteUser(String userId) throws IOException {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		// delete profile image
		String fullPath = imagePath + File.separator + user.getImage();
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
		} catch (NoSuchFileException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.userRepository.deleteById(userId);
	}
}