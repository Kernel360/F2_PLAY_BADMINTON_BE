package org.badminton.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	// 400 Errors
	BAD_REQUEST(400, "클라이언트 요청의 형식이나 내용이 잘못되었습니다."),
	INVALID_PARAMETER(400, "요청한 파라미터의 형식이나 내용에 오류가 있습니다."),
	INVALID_RESOURCE(400, "요청한 리소스의 내용에 오류가 있습니다."),
	MISSING_PARAMETER(400, "필수 파라미터가 지정되지 않았습니다."),
	LIMIT_EXCEEDED(400, "파라미터 또는 리소스 속성값이 제한을 초과했습니다."),
	OUT_OF_RANGE(400, "파라미터 또는 리소스 속성값이 범위를 벗어났습니다."),

	// 401 Errors
	UNAUTHORIZED(401, "요구되는 인증 정보가 누락되었거나 잘못되었습니다."),

	// 403 Errors
	FORBIDDEN(403, "요청이 거부되었습니다."),
	ACCESS_DENIED(403, "리소스에 대한 접근이 제한되었습니다."),
	LIMIT_EXCEEDED_403(403, "리소스의 제한 설정을 초과했습니다."),
	OUT_OF_RANGE_403(403, "리소스의 제한 범위를 벗어났습니다."),

	// 404 Errors
	NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
	JWT_COOKIE_NOT_FOUNT(404, "JWT 쿠키를 찾을 수 없습니다."),

	// RESOURCE_NOT_EXIST
	RESOURCE_NOT_EXIST(404, "특정 리소스를 찾을 수 없습니다."),
	MEMBER_NOT_EXIST(404, "해당하는 회원이 존재하지 않습니다."),
	CLUB_NOT_EXIST(404, "해당하는 동호회가 존재하지 않습니다."),

	// 409 Errors
	CONFLICT(409, "리소스 충돌이 발생했습니다."),
	ALREADY_EXIST(409, "리소스가 이미 존재합니다."),

	// RESOURCE_ALREADY_EXIST
	RESOURCE_ALREADY_EXIST(409, "특정 리소스가 이미 존재합니다."),
	CLUB_NAME_ALREADY_EXIST(409, "이미 존재하는 동호회 이름입니다."),

	// 410 Errors
	DELETED(410, "요청한 리소스가 삭제되었습니다."),

	// 500 Errors
	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),

	// 503 Errors
	SERVICE_UNAVAILABLE(503, "일시적인 서버 오류입니다.");

	private final int httpStatusCode;
	private final String description;
}
