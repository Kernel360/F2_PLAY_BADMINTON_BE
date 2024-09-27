package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class LeagueNotExistException extends BadmintonException {

	public LeagueNotExistException(Long clubId, Long leagueId) {
		super(ErrorCode.LEAGUE_NOT_EXIST, "[동호회 아이디 : " + clubId + " 경기 일정 아이디 : " + leagueId + "]");
	}

	public LeagueNotExistException(Long clubId, Long leagueId, Exception e) {
		super(ErrorCode.LEAGUE_NOT_EXIST, "[동호회 아이디 : " + clubId + " 경기 일정 아이디 : " + leagueId + "]", e);
	}
}
