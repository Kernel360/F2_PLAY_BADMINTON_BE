package org.badminton.domain.common.exception.member;

import org.badminton.domain.common.error.ErrorCode;
import org.badminton.domain.common.exception.BadmintonException;

public class MemberNotExistException extends BadmintonException {
	public MemberNotExistException(Long memberId) {
		super(ErrorCode.MEMBER_NOT_EXIST, "[아이디 : " + memberId + "]");
	}

	public MemberNotExistException(Long memberId, Exception e) {
		super(ErrorCode.MEMBER_NOT_EXIST, "[아이디 : " + memberId + "]", e);
	}
}
