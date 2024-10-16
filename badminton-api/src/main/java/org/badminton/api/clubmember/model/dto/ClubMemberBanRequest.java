package org.badminton.api.clubmember.model.dto;

import org.badminton.api.clubmember.validation.ReasonValidator;
import org.badminton.domain.clubmember.entity.BannedType;

public record ClubMemberBanRequest(

	BannedType type,

	@ReasonValidator
	String bannedReason
) {
}
