package com.quickpik.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quickpik.dtos.PageableResponse;
import com.quickpik.dtos.UserDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.FileService;
import com.quickpik.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

// The UserController class provides REST endpoints for user-related operations 
@RestController
@RequestMapping("/users")
public class UserController {

	@Value("${user-image-path}")
	private String imageUploadPath;

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	// Get a paginated list of all users, sorted and filtered according to given
	// parameters.
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "fname", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		// Call the UserService to get the paginated list of users
		PageableResponse<UserDto> users = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<UserDto>>(users, HttpStatus.OK);
	}

//	Get a user by user id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
		// Return the user in a ResponseEntity with HttpStatus.OK
		return new ResponseEntity<UserDto>(this.userService.getUserById(userId), HttpStatus.OK);
	}

//	Get a user by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<UserDto>(this.userService.getUserByEmail(email), HttpStatus.OK);
	}

//	Get a user by keyword matching the fname
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
		return new ResponseEntity<List<UserDto>>(this.userService.searchUser(keyword), HttpStatus.OK);
	}

// Create new user
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = this.userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	// Update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId,
			@RequestBody UserDto userDto) {
		UserDto user = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Delete user
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") String userId) throws IOException {
		UserDto user = userService.getUserById(userId);
		if (user == null) {
			ApiResponse response = ApiResponse.builder().message("User not found").status(HttpStatus.NOT_FOUND.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} else {
			this.userService.deleteUser(userId);
			ApiResponse response = ApiResponse.builder().message("User deleted successfully")
					.status(HttpStatus.OK.value()).timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	// Upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ApiResponse> uploadUserImage(@PathVariable("userId") String userId,
			@RequestParam("image") MultipartFile image) {
		String imageName;
		try {

			// upload image and returns image name
			imageName = this.fileService.uploadImage(image, imageUploadPath);

			// get user by id and set the image name for the user
			UserDto userDto = this.userService.getUserById(userId);
			userDto.setImage(imageName);
			userService.updateUser(userDto, userId);

			// Build the API response with success message and status code
			ApiResponse apiResponse = ApiResponse.builder().message(imageName).status(HttpStatus.CREATED.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CREATED);
		} catch (IOException e) {
			e.printStackTrace();
			// Build the API response with error message and status code
			ApiResponse apiResponse = ApiResponse.builder().message("Something went wrong! Please try again")
					.status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
		}
	}

	// Serve images
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

		// get the user by user id
		UserDto user = this.userService.getUserById(userId);

		// retrieve the user's image from the specified resource location
		InputStream resource = fileService.getResource(imageUploadPath, user.getImage());
		
		// set the response content type to image/jpeg
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		// copy the image data to the response output stream
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
