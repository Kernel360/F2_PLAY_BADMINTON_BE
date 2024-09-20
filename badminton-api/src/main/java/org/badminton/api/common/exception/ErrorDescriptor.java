package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCode;

public interface ErrorDescriptor {
	ErrorCode getErrorCode();

	String getErrorDetails();
}
