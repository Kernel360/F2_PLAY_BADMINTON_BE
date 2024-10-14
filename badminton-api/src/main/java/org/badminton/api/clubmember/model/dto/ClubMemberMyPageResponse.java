package org.badminton.api.clubmember.model.dto;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.common.enums.MemberTier;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubMemberMyPageResponse(
	@Schema(description = "Club ID", example = "1")
	Long clubId,

	@Schema(description = "Club member ID", example = "1")
	Long clubMemberId,

	@Schema(description = "Club name", example = "배드민턴 동호회")
	String clubName,

	@Schema(description = "Member role", example = "ROLE_USER")
	ClubMemberRole role,

	@Schema(description = "ClubMember Tier", example = "GOLD")
	MemberTier tier
) {
	public static ClubMemberMyPageResponse fromClubMemberEntity(ClubMemberEntity clubMemberEntity) {
		return new ClubMemberMyPageResponse(
			clubMemberEntity.getClub().getClubId(),
			clubMemberEntity.getClubMemberId(),
			clubMemberEntity.getClub().getClubName(),
			clubMemberEntity.getRole(),
			clubMemberEntity.getTier()
		);
	}
}
