package org.badminton.api.common.exception.oauth;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class JwtCookieNotFoundException extends BadmintonException {

	public JwtCookieNotFoundException(ErrorCode errorCode, String errorDetails) {
		super(errorCode, errorDetails);
	}
}

