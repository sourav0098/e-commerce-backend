package com.quickpik.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quickpik.dtos.UserDto;
import com.quickpik.payload.ApiResponse;
import com.quickpik.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers(){
		List<UserDto> users=this.userService.getAllUsers();
		return new ResponseEntity<List<UserDto>>(users,HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
		return new ResponseEntity<UserDto>(this.userService.getUserById(userId),HttpStatus.OK);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		return new ResponseEntity<UserDto>(this.userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword){
		return new ResponseEntity<List<UserDto>>(this.userService.searchUser(keyword),HttpStatus.OK);
	}
	
	
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto user=this.userService.createUser(userDto);
		return new ResponseEntity<>(user,HttpStatus.CREATED);
	}
	
	// update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId, @RequestBody UserDto userDto){
		UserDto user=this.userService.updateUser(userDto,userId);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	// delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") String userId){
		UserDto user = userService.getUserById(userId);
		if (user == null) {
			ApiResponse response = ApiResponse.builder().message("User not found").status(HttpStatus.NOT_FOUND.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}else {			
			this.userService.deleteUser(userId);
			ApiResponse response = ApiResponse.builder().message("User deleted successfully").status(HttpStatus.OK.value())
					.timestamp(System.currentTimeMillis()).build();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}	
}
