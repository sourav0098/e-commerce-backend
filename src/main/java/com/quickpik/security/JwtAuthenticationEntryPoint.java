package com.quickpik.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sourav Choudhary
 * 
 * A class that implements Spring AuthenticationEntryPoint interface to handle
 * unauthorized access attempts to a protected resource. This implementation
 * sets the HTTP status code to 401 (unauthorized) and sends a message to the
 * client indicating that the access is denied along with the exception message.
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * Called when an unauthorized access attempt to a protected resource is detected.
	 * Sets the HTTP status code to 401 (unauthorized) and sends a message to the client indicating
	 * that the access is denied along with the exception message.
	 */
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("Access Denied! " + authException.getMessage());
	}
}