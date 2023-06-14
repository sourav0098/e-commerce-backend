package com.quickpik.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.quickpik.dtos.JwtRequestDto;
import com.quickpik.dtos.JwtResponseDto;
import com.quickpik.dtos.UserDto;
import com.quickpik.dtos.UserResponseDto;
import com.quickpik.entities.AuthenticationProvider;
import com.quickpik.entities.User;
import com.quickpik.exception.UnauthorizedException;
import com.quickpik.security.JwtHelper;
import com.quickpik.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/auth")
@RestController
public class AuthController {

	@Value("${googleClientId}")
	private String googleClientId;
	
	@Value("${newPassword}")
	private String googlePassword;

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

	@PostMapping("/login")
	public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto request) {
		// call method to authenticate email and password
		this.doAuthenticate(request.getEmail(), request.getPassword());

		UserDetails userDetails = userDetailService.loadUserByUsername(request.getEmail());

		// generate jwt token and refresh token
		String jwtToken = this.jwtHelper.generateToken(userDetails);
		String refreshToken = this.jwtHelper.generateRefreshToken(userDetails);

		UserResponseDto userResponseDto = modelMapper.map(userDetails, UserResponseDto.class);

		JwtResponseDto response = JwtResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken)
				.user(userResponseDto).build();
		return new ResponseEntity<JwtResponseDto>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtResponseDto> refreshToken(HttpServletRequest request) {
		// Check if "Authorization" header is available
		String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String username = null;
		String refreshToken = null;

		// If the header is present, the code checks if token starts with the string
		// "Bearer". The code extracts the token from the header by removing the first
		// seven characters, which correspond to the "Bearer " prefix.
		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			refreshToken = requestHeader.substring(7);
			try {
				// get username from token
				username = this.jwtHelper.getUsernameFromToken(refreshToken);
			} catch (IllegalArgumentException ex) {
				log.info("Illegal argument while fetching username");
				ex.printStackTrace();
			} catch (ExpiredJwtException ex) {
				log.info("JWT Token is expired");
				ex.printStackTrace();
			} catch (MalformedJwtException ex) {
				log.info("Invalid token! Some changes has done in token");
				ex.printStackTrace();
			}
		} else {
			log.info("Invalid token");
		}

		// Check if username is not null and there is no authentication object inside
		// security context
		if (username != null) {
			// fetch user details from username
			UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
			UserResponseDto userDto = modelMapper.map(userDetails, UserResponseDto.class);

			// Validate token
			Boolean validateToken = this.jwtHelper.validateToken(refreshToken, userDetails);
			if (validateToken) {
				var accessToken = this.jwtHelper.generateToken(userDetails);
				JwtResponseDto jwtResponse = JwtResponseDto.builder().accessToken(accessToken)
						.refreshToken(refreshToken).user(userDto).build();
				return new ResponseEntity<JwtResponseDto>(jwtResponse, HttpStatus.OK);
			} else {
				log.info("Validation failed");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

// Login with Google API
	@PostMapping("/google/login")
	public ResponseEntity<JwtResponseDto> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {

		// Get idToken from request
		String idToken = data.get("credential").toString();

		NetHttpTransport netHttpTransport = new NetHttpTransport();
		GsonFactory gsonFactory = GsonFactory.getDefaultInstance();

		GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, gsonFactory)
				.setAudience(Collections.singleton(googleClientId));

		// Call google api to verify token
		GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
		GoogleIdToken.Payload payload = googleIdToken.getPayload();

		String email = payload.getEmail();
		String fname = (String) payload.get("given_name");
		String lname = (String) payload.get("family_name");

		User user = null;
		user = this.userService.findUserByEmailOptional(email).orElse(null);

		if (user == null) {
			// create new user
			user = this.saveUser(email, fname, lname);
		}

		ResponseEntity<JwtResponseDto> jwtResponseEntity = this
				.login(JwtRequestDto.builder().email(user.getEmail()).password(googlePassword).build());
		return jwtResponseEntity;
	}

	private User saveUser(String email, String fname, String lname) {
		UserDto newUser = UserDto.builder().fname(fname).lname(lname).email(email).password(googlePassword)
				.authenticationProvider(AuthenticationProvider.GOOGLE).roles(new HashSet<>()).build();
		UserDto user = userService.createUser(newUser);
		return this.modelMapper.map(user, User.class);
	}

	private void doAuthenticate(String email, String password) {
		// check if email and password are correct
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			this.manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			// throw this exception if invalid email or password
			throw new UnauthorizedException("Invalid email or password!");
		}
	}
}