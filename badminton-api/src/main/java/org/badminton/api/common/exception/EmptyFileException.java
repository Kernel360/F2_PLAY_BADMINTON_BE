package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public class EmptyFileException extends BadmintonException {
	public EmptyFileException() {
		super(ErrorCode.FILE_NOT_EXIST);
	}

	public EmptyFileException(Exception exception) {
		super(ErrorCode.FILE_NOT_EXIST, exception);
	}
}
