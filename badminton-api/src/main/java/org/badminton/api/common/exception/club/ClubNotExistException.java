package org.badminton.api.common.exception.club;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubNotExistException extends BadmintonException {

	public ClubNotExistException(Long clubId) {
		super(ErrorCode.CLUB_NOT_EXIST, ErrorCode.CLUB_NOT_EXIST.getDescription() + "[동호회 아이디 : " + clubId + "]");
	}

	public ClubNotExistException(Long clubId, Exception e) {
		super(ErrorCode.CLUB_NOT_EXIST, ErrorCode.CLUB_NOT_EXIST.getDescription() + "[동호회 아이디 : " + clubId + "]", e);
	}
}
