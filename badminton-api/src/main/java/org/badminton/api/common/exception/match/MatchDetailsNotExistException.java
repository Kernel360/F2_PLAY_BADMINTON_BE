package org.badminton.api.common.exception.match;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class MatchDetailsNotExistException extends BadmintonException {

	public MatchDetailsNotExistException(Long matchId) {
		super(ErrorCode.MATCH_DETAILS_NOT_EXIST, "[게임 아이디 : " + matchId + "]");
	}

	public MatchDetailsNotExistException(ErrorCode errorCode, String errorDetails, Exception e) {
		super(errorCode, errorDetails, e);
	}

}
