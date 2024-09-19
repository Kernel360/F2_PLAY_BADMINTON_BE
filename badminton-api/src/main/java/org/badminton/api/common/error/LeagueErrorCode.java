package org.badminton.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LeagueErrorCode implements ErrorCodeIfs {

	LEAGUE_NOT_FOUND(ErrorCode.BAD_REQUEST.getErrorCode(), ErrorCode.LEAGUE_NOT_FOUND.getErrorCode(),
		ErrorCode.LEAGUE_NOT_FOUND.getDescription()),
	LEAGUE_ALREADY_EXISTS(ErrorCode.BAD_REQUEST.getErrorCode(), ErrorCode.LEAGUE_ALREADY_EXISTS.getErrorCode(),
		ErrorCode.LEAGUE_ALREADY_EXISTS.getDescription());

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;
}
