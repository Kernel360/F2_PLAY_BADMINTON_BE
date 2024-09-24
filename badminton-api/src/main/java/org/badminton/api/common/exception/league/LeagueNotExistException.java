package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class LeagueNotExistException extends BadmintonException {

	public LeagueNotExistException(ErrorCode errorCode, String leagueId) {
		super(errorCode, "리그 아이디", leagueId);
	}

}
