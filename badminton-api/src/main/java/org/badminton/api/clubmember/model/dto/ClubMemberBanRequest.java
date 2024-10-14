package org.badminton.api.clubmember.model.dto;

import org.badminton.domain.clubmember.entity.BannedType;

public record ClubMemberBanRequest(
	BannedType type,
	String bannedReason
) {
}
