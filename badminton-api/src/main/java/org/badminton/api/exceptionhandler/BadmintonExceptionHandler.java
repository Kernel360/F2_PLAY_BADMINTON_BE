package org.badminton.api.exceptionhandler;

import org.badminton.api.common.exception.BadmintonException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class BadmintonExceptionHandler {

	@ExceptionHandler(value = BadmintonException.class)
	public ResponseEntity<String> handleBadmintonException(
		BadmintonException badmintonException
	) {
		var errorCode = badmintonException.getErrorCodeIfs();
		return ResponseEntity.status(errorCode.getHttpStatusCode())
			.body(
				"에러 코드 " + errorCode.getErrorCode() + ": " + badmintonException.getErrorDescription()
			);
	}
}
