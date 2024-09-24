package org.badminton.api.common.exception.club;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubNotExistException extends BadmintonException {

	public ClubNotExistException(ErrorCode errorCode, Long clubId) {
		super(errorCode, "동호회 아이디", String.valueOf(clubId));
	}

	public ClubNotExistException(ErrorCode errorCode, Long clubId, Exception e) {
		super(errorCode, "동호회 아이디", String.valueOf(clubId), e);
	}
}
