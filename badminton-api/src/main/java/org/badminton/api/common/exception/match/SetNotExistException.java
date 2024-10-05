package org.badminton.api.common.exception.match;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.common.enums.MatchType;

public class SetNotExistException extends BadmintonException {
	public SetNotExistException(Long leagueId, Long matchId, Long setId, MatchType matchType) {
		super(ErrorCode.SET_NOT_EXIST,
			"[경기 일정 아이디 : " + leagueId + " 대진 아이디 : " + matchId + " 세트 아이디 : " + setId + " 경기 타입 : " + matchType + "]");
	}

	public SetNotExistException(Long leagueId, Long matchId, Long setId, MatchType matchType, Exception e) {
		super(ErrorCode.SET_NOT_EXIST,
			"[경기 일정 아이디 : " + leagueId + " 대진 아이디 : " + matchId + " 세트 아이디 : " + setId + " 경기 타입 : " + matchType + "]",
			e);

	}

}
