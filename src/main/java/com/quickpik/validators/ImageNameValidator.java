package com.quickpik.validators;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// Logic
		if (value.isBlank()) {
			logger.info("Blank image name");
			return false;
		} else {
			logger.info("Valid image name");
			return true;
		}
	}

}
