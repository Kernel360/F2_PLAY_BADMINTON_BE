package org.badminton.api.clubmember.validation;

import org.badminton.api.club.validation.ClubNameValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReasonValidatorImpl implements ConstraintValidator<ReasonValidator, String> {

	@Override
	public void initialize(ReasonValidator constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}
		return value.trim().length() >= 2 && value.trim().length() <= 100;
	}
}
