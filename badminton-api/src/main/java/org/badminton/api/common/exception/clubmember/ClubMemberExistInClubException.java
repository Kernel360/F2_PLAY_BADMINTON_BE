package org.badminton.api.common.exception.clubmember;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubMemberExistInClubException extends BadmintonException {

	public ClubMemberExistInClubException(Long memberId) {
		super(ErrorCode.MEMBER_ALREADY_EXIST_IN_CLUB, "[회원 아이디 : " + memberId + "]");
	}

	public ClubMemberExistInClubException(Long memberId, Exception e) {
		super(ErrorCode.MEMBER_ALREADY_EXIST_IN_CLUB, "[회원 아이디 : " + memberId + "]", e);
	}
}
