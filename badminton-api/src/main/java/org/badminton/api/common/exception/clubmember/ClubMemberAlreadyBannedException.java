package org.badminton.api.common.exception.clubmember;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubMemberAlreadyBannedException extends BadmintonException {

	public ClubMemberAlreadyBannedException(Long clubMemberId) {
		super(ErrorCode.CLUB_MEMBER_ALREADY_BANNED, "[동호회 회원 아이디: " + clubMemberId + "]");
	}

	public ClubMemberAlreadyBannedException(Long clubMemberId, Exception e) {
		super(ErrorCode.CLUB_MEMBER_ALREADY_BANNED, "[동호회 회원 아이디: " + clubMemberId + "]");
	}
}
