package com.quickpik.exception;

/**
 * @author Sourav Choudhary
 * <p>
 * This exception is thrown when a validation constraint is violated in a database operation.
 * */

public class ConstraintViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public ConstraintViolationException() {
		super("Unique constraint voilation");
	}
	
	public ConstraintViolationException(String message) {
		super(message);
	}
}
