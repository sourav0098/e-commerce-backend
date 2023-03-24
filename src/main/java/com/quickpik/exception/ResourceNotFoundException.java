package com.quickpik.exception;

/**
 * This class represents a custom exception that can be thrown if a resource is
 * not found on the server.
 * 
 * @author Sourav Choudhary
 */

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("Resource Not Found on Server");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
