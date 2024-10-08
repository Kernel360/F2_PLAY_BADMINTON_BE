package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class InvalidDateTimeException extends BadmintonException {
	public InvalidDateTimeException(int year, int month) {
		super(ErrorCode.INVALID_RESOURCE, "[유효하지 않은 날짜 : " + year + "년 " + month + "월]");
	}

	public InvalidDateTimeException(int year, int month, Exception exception) {
		super(ErrorCode.INVALID_RESOURCE, "[유효하지 않은 날짜 : " + year + "년 " + month + "월]", exception);
	}
}
