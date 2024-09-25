package org.badminton.api.common.exception.member;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class MemberNotExistException extends BadmintonException {

	public MemberNotExistException(Long memberId) {
		super(ErrorCode.MEMBER_NOT_EXIST, ErrorCode.MEMBER_NOT_EXIST.getDescription() + "[회원 아이디 : " + memberId + "]");
	}

	public MemberNotExistException(Long memberId, Exception e) {
		super(ErrorCode.MEMBER_NOT_EXIST, ErrorCode.MEMBER_NOT_EXIST.getDescription() + "[회원 아이디 : " + memberId + "]",
			e);
	}

	public MemberNotExistException(String providerId) {
		super(ErrorCode.MEMBER_NOT_EXIST,
			ErrorCode.MEMBER_NOT_EXIST.getDescription() + "[회원 소셜 아이디 : " + providerId + "]");
	}

	public MemberNotExistException(String providerId, Exception e) {
		super(ErrorCode.MEMBER_NOT_EXIST,
			ErrorCode.MEMBER_NOT_EXIST.getDescription() + "[회원 소셜 아이디 : " + providerId + "]");
	}
}
