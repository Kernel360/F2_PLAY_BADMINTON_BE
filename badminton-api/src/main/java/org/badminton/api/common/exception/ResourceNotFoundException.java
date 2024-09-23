package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class ResourceNotFoundException extends BadmintonException {
	public ResourceNotFoundException(ErrorCode errorCode, String typeName, Long resourceId) {
		super(errorCode, typeName, String.valueOf(resourceId));
	}
}
