package com.quickpik.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	// This method checks if the image name is valid according to the
	// validation rules specified by the ImageNameValid annotation
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// If the value is blank, it's considered invalid
		if (value.isBlank()) {
			return false;
		} else {
			// Otherwise, it's considered valid
			return true;
		}
	}
}
