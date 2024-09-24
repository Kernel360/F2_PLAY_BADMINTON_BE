package org.badminton.api.common.exception.club;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;

public class ClubNameDuplicateException extends BadmintonException {

	public ClubNameDuplicateException(ErrorCode errorCode, String clubName) {
		super(errorCode, "동호회 이름", clubName);
	}

	public ClubNameDuplicateException(ErrorCode errorCode, String clubName, Exception e) {
		super(errorCode, "동호회 이름", clubName, e);
	}
}
