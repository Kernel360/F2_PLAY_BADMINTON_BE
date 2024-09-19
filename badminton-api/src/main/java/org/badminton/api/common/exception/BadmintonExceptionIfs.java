package org.badminton.api.common.exception;

import org.badminton.api.common.error.ErrorCodeIfs;

public interface BadmintonExceptionIfs {
	ErrorCodeIfs getErrorCodeIfs();

	String getErrorDescription();
}
