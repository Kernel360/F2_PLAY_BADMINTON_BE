package org.badminton.api.common.exception.member;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class MemberNotExistException extends BadmintonException {

	public MemberNotExistException(ErrorCode errorCode, Long providerId, Exception e) {
		super(errorCode, "providerId", String.valueOf(providerId), e);
	}

	public MemberNotExistException(ErrorCode errorCode, String providerId) {
		super(errorCode, "providerId", String.valueOf(providerId));
	}

}
