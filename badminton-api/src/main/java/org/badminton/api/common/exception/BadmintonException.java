package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String errorDetails;

	public BadmintonException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		this.errorDetails = errorCode.getDescription() + wrapErrorDetails(typeName) + resourceName + "]";
	}

	public BadmintonException(ErrorCode errorCode, String errorDetails) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		this.errorDetails = errorCode.getDescription() + "[ " + errorDetails + " ]";
	}

	private String wrapErrorDetails(String typeName) {
		return "[" + typeName + ": ";
	}
}
