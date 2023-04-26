package com.quickpik.exception;

public class BadApiRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public BadApiRequestException() {
		super("Bad request");
	}
	
	public BadApiRequestException(String message) {
		super(message);
	}
}