package org.badminton.api.member.model.dto;

import org.badminton.domain.clubmember.entity.ClubMemberRole;

public record MemberIsClubMemberResponse(
	boolean isClubMember,
	ClubMemberRole role,
	Long clubId
) {
}
