package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class DuplicationException extends BadmintonException {
	public DuplicationException(ErrorCode errorCode, String target) {
		super(errorCode, target);
	}
}
