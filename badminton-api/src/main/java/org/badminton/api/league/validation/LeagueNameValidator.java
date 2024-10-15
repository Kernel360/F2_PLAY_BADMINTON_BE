package org.badminton.api.league.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = {LeagueNameValidatorImpl.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LeagueNameValidator {
	String message() default "동호회 이름 조건을 확인해주세요."; // 기본 에러 메시지

	Class<?>[] groups() default {}; // 그룹을 지정할 때 사용

	Class<? extends Payload>[] payload() default {}; // 페이로드를 전달할 때 사용
}
