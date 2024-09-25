package org.badminton.api.common.exception.club;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubNameDuplicateException extends BadmintonException {

	public ClubNameDuplicateException(String clubName) {
		super(ErrorCode.CLUB_NAME_ALREADY_EXIST, ErrorCode.LEAGUE_ALREADY_EXIST.getDescription() + "[ : " +
			clubName + "]");
	}

	public ClubNameDuplicateException(String clubName, Exception e) {
		super(ErrorCode.CLUB_NAME_ALREADY_EXIST, ErrorCode.LEAGUE_ALREADY_EXIST.getDescription() + "[ : " +
			clubName + "]", e);
	}
}