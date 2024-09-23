package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class OAuthException extends BadmintonException {
	public OAuthException(ErrorCode errorCode) {
		super(errorCode, "oAuth 로그인에 실패했습니다.");
	}
}
