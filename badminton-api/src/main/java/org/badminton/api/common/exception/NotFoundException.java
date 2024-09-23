package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class NotFoundException extends BadmintonException {
	public NotFoundException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode, typeName, resourceName);
	}
}
