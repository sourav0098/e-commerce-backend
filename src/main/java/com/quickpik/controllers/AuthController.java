package com.quickpik.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickpik.dtos.JwtRequestDto;
import com.quickpik.dtos.JwtResponseDto;
import com.quickpik.dtos.UserDto;
import com.quickpik.exception.BadApiRequestException;
import com.quickpik.security.JwtHelper;
import com.quickpik.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtHelper jwtHelper;

	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<UserDto>(modelMapper.map(userDetailService.loadUserByUsername(name), UserDto.class),
				HttpStatus.OK);
	}

	@GetMapping("/login")
	public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto request) {
		this.doAuthenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());
		String token = this.jwtHelper.generateToken(userDetails);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		JwtResponseDto response = JwtResponseDto.builder().jwtToken(token).user(userDto).build();
		return new ResponseEntity<JwtResponseDto>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			this.manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException("Invalid email or password!!");
		}
	}

}