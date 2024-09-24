package org.badminton.api.common.exception.oauth;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class JwtCookieNotFoundException extends BadmintonException {
	public JwtCookieNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public JwtCookieNotFoundException(ErrorCode errorCode, Exception e) {
		super(errorCode, e);
	}
}

