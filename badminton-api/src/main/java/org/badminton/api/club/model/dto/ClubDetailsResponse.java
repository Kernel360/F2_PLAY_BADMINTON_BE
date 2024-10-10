package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.enums.MemberTier;

public record ClubDetailsResponse(
	String clubName,
	String clubDescription,
	String clubImage,
	ClubMemberCountByTier clubMemberCountByTier,
	int clubMemberCount,
	LocalDateTime createdAt
) {

	public static ClubDetailsResponse clubEntityToClubReadResponse(ClubEntity clubEntity,
		Map<MemberTier, Long> memberCountByTier) {
		return new ClubDetailsResponse(clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			ClubMemberCountByTier.ofClubMemberCountResponse(memberCountByTier),
			clubEntity.getClubMembers().size(),
			clubEntity.getCreatedAt());
	}
}
