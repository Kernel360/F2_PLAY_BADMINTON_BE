package org.badminton.api.common.exception.clubmember;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubMemberExistInClubException extends BadmintonException {

	public ClubMemberExistInClubException(ErrorCode errorCode, Long memberId) {
		super(errorCode, "멤버 아이디", String.valueOf(memberId));
	}

	public ClubMemberExistInClubException(ErrorCode errorCode, Long memberId, Exception e) {
		super(errorCode, "멤버 아이디", String.valueOf(memberId), e);
	}
}
