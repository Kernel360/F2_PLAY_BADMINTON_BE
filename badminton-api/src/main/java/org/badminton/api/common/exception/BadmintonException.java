package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String errorDetails;
	private final String typeName;
	private final String resourceName;

	public BadmintonException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode.getDescription());
		this.errorCode = errorCode;
		this.typeName = typeName;
		this.resourceName = resourceName;
		this.errorDetails = errorCode.getDescription() + wrapErrorDetails(typeName) + resourceName + "]";
	}

	private String wrapErrorDetails(String typeName) {
		return "[" + typeName + ": ";
	}
}
