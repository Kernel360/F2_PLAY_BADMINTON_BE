package org.badminton.api.club.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ClubDescriptionValidatorImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClubDescriptionValidator {

	String message() default "동호회 소개란은 2자 이상 입력해야 하며 최대 1000자까지 입력할 수 있습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
