package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String errorMessage;

	public BadmintonException(ErrorCode errorCode, String typeName, String resourceName) {
		this(errorCode, typeName, resourceName, null);
	}

	public BadmintonException(ErrorCode errorCode, String typeName, String resourceName, Exception e) {
		super(errorCode.getDescription(), e);
		this.errorCode = errorCode;
		this.errorMessage =
			errorCode.getDescription() + wrapTypeAndResourceNameToErrorMessage(typeName, resourceName);
	}

	private String wrapTypeAndResourceNameToErrorMessage(String typeName, String resourceName) {
		return "[" + typeName + ": " + resourceName + "]";
	}
}
