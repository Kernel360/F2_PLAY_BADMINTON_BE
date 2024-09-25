package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class LeagueNotExistException extends BadmintonException {

	public LeagueNotExistException(Long leagueId) {
		super(ErrorCode.LEAGUE_ALREADY_EXIST, "[경기 일정 아이디 : " + leagueId + "]");
	}

	public LeagueNotExistException(Long leagueId, Exception e) {
		super(ErrorCode.LEAGUE_ALREADY_EXIST, "[경기 일정 아이디 : " + leagueId + "]", e);
	}
}
