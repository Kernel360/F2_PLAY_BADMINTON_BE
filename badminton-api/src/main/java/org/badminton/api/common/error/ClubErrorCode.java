package org.badminton.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements ErrorCodeIfs {

	CLUB_NOT_FOUND(ErrorCode.BAD_REQUEST.getErrorCode(), ErrorCode.CLUB_NOT_FOUND.getErrorCode(),
		ErrorCode.CLUB_NOT_FOUND.getDescription()),
	CLUB_ALREADY_EXISTS(ErrorCode.BAD_REQUEST.getErrorCode(), ErrorCode.CLUB_ALREADY_EXISTS.getErrorCode(),
		ErrorCode.CLUB_ALREADY_EXISTS.getDescription());

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
