package com.quickpik.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// For any incoming request JWT Authentication Filter class gets executed and checks if the JWT Token is valid or not. 
// If the token is valid it then sets the authentication to SecurityContext to specify that current user is validated

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDetailsService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Check if "Authorization" header is available
		String requestHeader = request.getHeader("Authorization");
		
		String username = null;
		String token = null;

		// If the header is present, the code checks if token starts with the string
		// "Bearer". The code extracts the token from the header by removing the first
		// seven characters, which correspond to the "Bearer " prefix.
		if (requestHeader != null && requestHeader.trim().startsWith("Bearer")) {
			token = requestHeader.substring(7);
			try {
				// get username from token
				username = this.jwtHelper.getUsernameFromToken(token);

			} catch (IllegalArgumentException ex) {
				logger.info("Illegal argument while fetching username");
				ex.printStackTrace();
			} catch (ExpiredJwtException ex) {
				logger.info("JWT Token is expired");
				ex.printStackTrace();
			} catch (MalformedJwtException ex) {
				logger.info("Invalid token! Some changes has done in token");
				ex.printStackTrace();
			}
		}
		
		// Check if username is not null and there is no authentication object inside
		// security context
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// fetch user details from username
			UserDetails userDetails = this.userDetailService.loadUserByUsername(username);

			// Validate token
			Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
			if (validateToken) {
				// if token is valid set authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {
				logger.info("Validation failed");
			}
		}

		filterChain.doFilter(request, response);

	}
}
