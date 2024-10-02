package org.badminton.api.common.exception.match;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.common.enums.MatchType;

public class MatchDuplicateException extends BadmintonException {

	public MatchDuplicateException(MatchType matchType, Long matchId) {
		super(ErrorCode.MATCH_ALREADY_EXIST, "[경기 타입 : " + matchType.getDescription() + " 게임 아이디 : " + matchId + "]");
	}

	public MatchDuplicateException(MatchType matchType, Long matchId, Exception e) {
		super(ErrorCode.MATCH_ALREADY_EXIST, "[경기 타입 : " + matchType.getDescription() + " 게임 아이디 : " + matchId + "]",
			e);

	}
}
