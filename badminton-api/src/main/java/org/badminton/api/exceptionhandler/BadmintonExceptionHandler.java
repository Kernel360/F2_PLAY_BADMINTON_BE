package org.badminton.api.exceptionhandler;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.api.common.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class BadmintonExceptionHandler {
	private static final String UNKNOWN_ERROR = "알 수 없는 에러";

	@ExceptionHandler(value = BadmintonException.class)
	public ResponseEntity<ExceptionResponse> handleBadmintonException(
		BadmintonException badmintonException
	) {
		var errorCode = badmintonException.getErrorCode();
		var errorDetails = badmintonException.getErrorDetails();
		log.error(wrapLogMessage(errorCode, errorDetails));
		return ResponseEntity.status(errorCode.getHttpStatusCode())
			.body(
				ExceptionResponse.builder()
					.httpStatusCode(errorCode.getHttpStatusCode())
					.errorCode(errorCode)
					.errorDetails(errorDetails)
					.build()
			);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleGlobalException(Exception e) {
		var unknownError = ErrorCode.INTERNAL_SERVER_ERROR;
		log.error(wrapLogMessage(unknownError, e.getMessage()));
		return ResponseEntity
			.status(unknownError.getHttpStatusCode())
			.body(ExceptionResponse.builder()
				.errorDetails(e.getMessage() + UNKNOWN_ERROR)
				.errorCode(unknownError)
				.httpStatusCode(unknownError.getHttpStatusCode())
				.build());
	}

	private String wrapLogMessage(ErrorCode errorCode, String errorDetails) {
		return "예외 발생: " + errorCode + " 예외 상세: " + errorDetails;
	}
}
