package org.badminton.api.common.exception.club;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubNotExistException extends BadmintonException {

	public ClubNotExistException(Long clubId) {
		super(ErrorCode.CLUB_NOT_EXIST, "동호회 아이디", String.valueOf(clubId));
	}

	public ClubNotExistException(Long clubId, Exception e) {
		super(ErrorCode.CLUB_NOT_EXIST, "동호회 아이디", String.valueOf(clubId), e);
	}
}
