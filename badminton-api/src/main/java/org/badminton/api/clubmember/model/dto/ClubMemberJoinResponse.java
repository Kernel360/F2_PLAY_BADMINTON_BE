package org.badminton.api.clubmember.model.dto;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public record ClubMemberJoinResponse(
	Long clubMemberId,
	String role
) {
	public static ClubMemberJoinResponse clubMemberEntityToClubMemberJoinResponse(
		ClubMemberEntity clubMemberEntity) {
		return new ClubMemberJoinResponse(
			clubMemberEntity.getClubMemberId(),
			clubMemberEntity.getRole().name()
		);

	}
}
