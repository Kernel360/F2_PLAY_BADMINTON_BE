package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class LeagueParticipationDuplicateException extends BadmintonException {

	public LeagueParticipationDuplicateException(Long leagueId, Long memberId) {
		super(ErrorCode.LEAGUE_ALREADY_PARTICIPATED, "[경기 일정 아이디 : " + leagueId + ", 회원 아이디 : " + memberId + "]");
	}

	public LeagueParticipationDuplicateException(Long leagueId, Long memberId, Exception e) {
		super(ErrorCode.LEAGUE_ALREADY_PARTICIPATED, "[경기 일정 아이디 : " + leagueId + ", 회원 아이디 : " + memberId + "]", e);
	}
}
