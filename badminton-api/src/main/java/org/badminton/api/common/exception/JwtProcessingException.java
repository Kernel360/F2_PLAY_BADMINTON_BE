package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class JwtProcessingException extends BadmintonException {
	public JwtProcessingException(ErrorCode errorCode) {
		super(errorCode, "jwt 토큰을 가공할 수 없습니다");
	}
}
