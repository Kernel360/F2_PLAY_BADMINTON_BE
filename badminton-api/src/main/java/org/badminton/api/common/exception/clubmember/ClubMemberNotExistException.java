package org.badminton.api.common.exception.clubmember;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubMemberNotExistException extends BadmintonException {

	public ClubMemberNotExistException(Long clubId, Long memberId) {
		super(ErrorCode.CLUB_MEMBER_NOT_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberId + "]");
	}

	public ClubMemberNotExistException(Long clubId, Long memberId, Exception e) {
		super(ErrorCode.CLUB_MEMBER_NOT_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberId + "]", e);
	}

	public ClubMemberNotExistException(Long clubMemberId) {
		super(ErrorCode.CLUB_MEMBER_NOT_EXIST, "[동호회 회원 아이디 : " + clubMemberId + "]");
	}

	public ClubMemberNotExistException(Long clubMemberId, Exception e) {
		super(ErrorCode.CLUB_MEMBER_NOT_EXIST, "[동호회 회원 아이디 : " + clubMemberId + "]", e);
	}

}
