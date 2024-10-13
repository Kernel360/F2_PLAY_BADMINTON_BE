package org.badminton.api.clubmember.model.dto;

import org.badminton.domain.clubmember.entity.ClubMemberRole;

public record ClubMemberRoleUpdateRequest(
	ClubMemberRole role
) {

}
