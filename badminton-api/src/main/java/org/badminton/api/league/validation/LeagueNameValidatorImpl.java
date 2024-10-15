package org.badminton.api.league.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LeagueNameValidatorImpl implements ConstraintValidator<LeagueNameValidator, String> {

	@Override
	public void initialize(LeagueNameValidator constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String leagueName, ConstraintValidatorContext context) {
		if (leagueName == null || leagueName.isBlank()) {
			setCustomMessage(context, "경기 이름은 공백일 수 없습니다.");
			return false;
		}

		if (leagueName.length() < 2 || leagueName.length() > 20) {
			setCustomMessage(context, "경기 이름은 최소 2글자 이상, 최대 20글자 이하여야 합니다.");
			return false;
		}

		return true;
	}

	private void setCustomMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
			.addConstraintViolation();
	}
}
