package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String errorDetails;

	public BadmintonException(ErrorCode errorCode, String errorDetails) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		this.errorDetails = errorCode.getDescription() + wrapErrorDetails(errorDetails);
	}

	private String wrapErrorDetails(String target) {
		return "[" + target + "]";
	}
}
