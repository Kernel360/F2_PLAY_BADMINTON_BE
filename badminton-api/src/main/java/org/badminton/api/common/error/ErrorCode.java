package org.badminton.api.common.error;

public enum ErrorCode {

	OK(200, "성공"),
	BAD_REQUEST(400, "잘못된 요청"),
	SERVER_ERROR(500, "서버 에러"),
	NULL_POINT(512, "Null Point"),
	LEAGUE_ALREADY_EXISTS(1400, "이미 존재하는 경기입니다."),
	LEAGUE_NOT_FOUND(1401, "존재하지 않은 경기입니다."),
	CLUB_ALREADY_EXISTS(1402, "이미 존재하는 동호회입니다."),
	CLUB_NOT_FOUND(1404, "존재하지 않은 동호회입니다.");

	private final Integer errorCode;
	private final String description;

	ErrorCode(Integer errorCode, String description) {
		this.errorCode = errorCode;
		this.description = description;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getDescription() {
		return description;
	}
}
