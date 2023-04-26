package com.quickpik.exception;

public class UnauthorizedException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public UnauthorizedException() {
		super("Unauthorized");
	}
	
	public UnauthorizedException(String message) {
		super(message);
	}
}
