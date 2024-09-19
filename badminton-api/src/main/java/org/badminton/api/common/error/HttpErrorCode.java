package org.badminton.api.common.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpErrorCode implements ErrorCodeIfs {

	OK(HttpStatus.OK.value(), ErrorCode.OK.getErrorCode(), ErrorCode.BAD_REQUEST.getDescription()),
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), ErrorCode.BAD_REQUEST.getErrorCode(),
		ErrorCode.BAD_REQUEST.getDescription()),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SERVER_ERROR.getErrorCode(),
		ErrorCode.SERVER_ERROR.getDescription()),
	NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.NULL_POINT.getErrorCode(),
		ErrorCode.NULL_POINT.getDescription()),
	;

	private final Integer httpStatusCode;
	private final Integer errorCode;
	private final String description;

}
