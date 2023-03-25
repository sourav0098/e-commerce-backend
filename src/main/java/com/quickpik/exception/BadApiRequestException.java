package com.quickpik.exception;

public class BadApiRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public BadApiRequestException() {
		super("Unique constraint voilation");
	}
	
	public BadApiRequestException(String message) {
		super(message);
	}
}
