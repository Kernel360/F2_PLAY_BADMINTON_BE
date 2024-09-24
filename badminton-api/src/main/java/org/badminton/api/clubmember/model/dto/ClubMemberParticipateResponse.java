package org.badminton.api.clubmember.model.dto;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public record ClubMemberParticipateResponse(
	Long clubMemberId,
	String role
) {
	public static ClubMemberParticipateResponse clubMemberEntityToClubMemberParticipateResponse(
		ClubMemberEntity clubMemberEntity) {
		return new ClubMemberParticipateResponse(
			clubMemberEntity.getClubMemberId(),
			clubMemberEntity.getRole().name()
		);

	}
}
