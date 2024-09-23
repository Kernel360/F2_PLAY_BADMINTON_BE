package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class JwtProcessingException extends BadmintonException {
	public JwtProcessingException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode, typeName, resourceName);
	}
}
