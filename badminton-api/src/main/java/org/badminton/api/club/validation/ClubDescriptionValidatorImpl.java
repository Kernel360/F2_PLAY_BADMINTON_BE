package org.badminton.api.club.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClubDescriptionValidatorImpl implements ConstraintValidator<ClubDescriptionValidator, String> {

	@Override
	public void initialize(ClubDescriptionValidator constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}
		return value.trim().length() >= 2 && value.trim().length() <= 1000;
	}
}
