package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class OAuthSuccessHandlingException extends BadmintonException {
	public OAuthSuccessHandlingException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode, typeName, resourceName);
	}
}
