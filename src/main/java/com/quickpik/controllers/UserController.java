package com.quickpik.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.quickpik.dtos.UserResponseDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.ImageService;
import com.quickpik.services.UserService;

// The UserController class provides REST endpoints for user-related operations 
@RestController
@RequestMapping("/users")
public class UserController {

	@Value("${aws.s3.user-image-path}")
	private String userImageUploadPath;

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	// Get a paginated list of all users, sorted and filtered according to given
	// parameters.
	@GetMapping
	public ResponseEntity<PageableResponse<UserResponseDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "fname", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		// Call the UserService to get the paginated list of users
		PageableResponse<UserResponseDto> users = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<UserResponseDto>>(users, HttpStatus.OK);
	}

//	Get a user by user id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
		// Return the user in a ResponseEntity with HttpStatus.OK
		return new ResponseEntity<UserDto>(this.userService.getUserById(userId), HttpStatus.OK);
	}

//	Get a user by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<UserResponseDto>(this.userService.getUserByEmail(email), HttpStatus.OK);
	}

//	Get a user by keyword matching the fname
	@GetMapping("/search/{fname}")
	public ResponseEntity<PageableResponse<UserResponseDto>> searchUser(@PathVariable String fname,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "fname", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<UserResponseDto> response = this.userService.searchUser(fname, pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<PageableResponse<UserResponseDto>>(response, HttpStatus.OK);
	}

// Create new user
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = this.userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	// Update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,
			@Valid @RequestBody UserDto userDto) {
		UserDto user = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ApiResponse> uploadUserImage(@PathVariable("userId") String userId,
			@RequestParam("image") MultipartFile image) {
		String imageName;
		try {

			// get user by id and set the image name for the user
			UserDto userDto = this.userService.getUserById(userId);

			String existingImageName = userDto.getImage();
			
			// upload new image and returns image name
			imageName = this.imageService.uploadImage(image, userImageUploadPath);

			// delete the existing image if it exists
			if (existingImageName != null && !existingImageName.isEmpty()) {
				System.out.println(existingImageName);
				this.imageService.deleteExistingImage(existingImageName, userImageUploadPath);
			}

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
	public ResponseEntity<ApiResponse> serveUserImage(@PathVariable String userId) throws IOException {
		// get the user by user id
		UserDto user = this.userService.getUserById(userId);
		String imageName = this.imageService.getImageUrl(user.getImage(), userImageUploadPath);
		ApiResponse apiResponse = ApiResponse.builder().message(imageName).status(HttpStatus.OK.value())
				.timestamp(System.currentTimeMillis()).build();
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}
}
