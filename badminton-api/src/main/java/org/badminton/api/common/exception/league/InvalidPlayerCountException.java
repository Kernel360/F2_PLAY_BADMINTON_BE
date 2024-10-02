package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class InvalidPlayerCountException extends BadmintonException {
	public InvalidPlayerCountException(Long leagueId, int playerCount) {
		super(ErrorCode.INVALID_PLAYER_COUNT, "[경기 일정 아이디 : " + leagueId + " 현재 모집 인원 : " + playerCount + "]");
	}

	public InvalidPlayerCountException(Long leagueId, Long playerCount, Exception e) {
		super(ErrorCode.INVALID_PLAYER_COUNT, "[경기 일정 아이디 : " + leagueId + " 현재 모집 인원 : " + playerCount + "]", e);
	}
}
