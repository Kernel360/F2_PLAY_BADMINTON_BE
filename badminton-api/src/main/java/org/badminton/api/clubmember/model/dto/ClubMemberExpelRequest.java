package org.badminton.api.clubmember.model.dto;

import org.badminton.api.clubmember.validation.ReasonValidator;

public record ClubMemberExpelRequest(
	@ReasonValidator
	String expelReason
) {
}
