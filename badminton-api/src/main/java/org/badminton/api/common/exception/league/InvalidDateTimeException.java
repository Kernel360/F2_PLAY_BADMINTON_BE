package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class InvalidDateTimeException extends BadmintonException {
	public InvalidDateTimeException(String date) {
		super(ErrorCode.INVALID_RESOURCE, "[유효하지 않은 날짜 : " + date + "]");
	}

	public InvalidDateTimeException(String date, Exception exception) {
		super(ErrorCode.INVALID_RESOURCE, "[유효하지 않은 날짜 : " + date + "]", exception);
	}
}
