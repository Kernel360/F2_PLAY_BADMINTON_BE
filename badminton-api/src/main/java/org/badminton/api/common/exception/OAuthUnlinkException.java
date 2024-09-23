package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class OAuthUnlinkException extends BadmintonException {
	public OAuthUnlinkException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode, typeName, resourceName);
	}
}
