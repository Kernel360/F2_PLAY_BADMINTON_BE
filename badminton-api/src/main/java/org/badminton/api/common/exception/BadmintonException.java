package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCodeIfs;

import lombok.Getter;

@Getter
public class BadmintonException extends RuntimeException implements BadmintonExceptionIfs {

	private final ErrorCodeIfs errorCodeIfs;
	private final String errorDescription;

	public BadmintonException(ErrorCodeIfs errorCodeIfs) {
		super(errorCodeIfs.getDescription());
		this.errorCodeIfs = errorCodeIfs;
		this.errorDescription = errorCodeIfs.getDescription();
	}
}
