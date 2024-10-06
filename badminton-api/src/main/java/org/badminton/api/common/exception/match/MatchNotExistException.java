package org.badminton.api.common.exception.match;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.common.enums.MatchType;

public class MatchNotExistException extends BadmintonException {
	public MatchNotExistException(Long matchId, MatchType matchType) {
		super(ErrorCode.MATCH_NOT_EXIST,
				"[경기 일정 아이디 : " + matchId + " 대진 아이디 : " + matchId + " 경기 타입 : "
						+ matchType.getDescription());
	}

	public MatchNotExistException(Long clubId, Long leagueId, Long matchId, MatchType matchType) {
		super(ErrorCode.MATCH_NOT_EXIST,
				"[동호회 아이디 : " + leagueId + " 경기 일정 아이디 : " + matchId + " 대진 아이디 : " + matchId + " 경기 타입 : "
						+ matchType.getDescription());
	}

	public MatchNotExistException(Long clubId, Long leagueId, Long matchId, MatchType matchType, Exception e) {
		super(ErrorCode.MATCH_NOT_EXIST,
			"[동호회 아이디 : " + leagueId + " 경기 일정 아이디 : " + matchId + " 대진 아이디 : " + matchId + " 경기 타입 : "
				+ matchType.getDescription(), e);
	}

}
