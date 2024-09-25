package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String errorMessage;

	public BadmintonException(ErrorCode errorCode, String errorMessage) {
		this(errorCode, errorMessage, null);
	}

	public BadmintonException(ErrorCode errorCode, String errorMessage, Exception e) {
		super(errorCode.getDescription(), e);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
