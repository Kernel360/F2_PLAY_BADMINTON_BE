package org.badminton.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements ErrorCodeIfs {

	CLUB_NOT_FOUND(400, 1404, "존재하지 않는 동호회입니다."),
	CLUB_ALREADY_EXISTS(400, 1402, "이미 존재하는 동호회입니다."),
	;

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
