package org.badminton.api.common.exception.member;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class MemberNotExistException extends BadmintonException {
	public MemberNotExistException(ErrorCode errorCode, String errorDetails) {
		super(errorCode, errorDetails);
	}

	public MemberNotExistException(ErrorCode errorCode, String typeName, String resourceName) {
		super(errorCode, typeName, resourceName);
	}
}
