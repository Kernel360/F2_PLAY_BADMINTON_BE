package org.badminton.api.clubmember.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.badminton.api.club.validation.ClubNameValidatorImpl;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ReasonValidatorImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReasonValidator {

	String message() default "회원 제제 사유는 2자 이상 100자 이하여야합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
