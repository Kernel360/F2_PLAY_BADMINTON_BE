package org.badminton.api.common.response;

import org.badminton.api.common.error.ErrorCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {
	private int httpStatusCode;
	private ErrorCode errorCode;
	private String errorMessage;
}
