package org.badminton.api.common.exception.member;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class MemberNotExistException extends BadmintonException {

	public MemberNotExistException(ErrorCode errorCode, Long memberId, Exception e) {
		super(errorCode, "회원", String.valueOf(memberId), e);
	}

	public MemberNotExistException(ErrorCode errorCode, Long memberId) {
		super(errorCode, "회원", String.valueOf(memberId));
	}

	public MemberNotExistException(ErrorCode errorCode, String providerId) {
		super(errorCode, "회원", providerId);
	}

	public MemberNotExistException(ErrorCode errorCode, String providerId, Exception e) {
		super(errorCode, "회원", providerId, e);
	}
}
