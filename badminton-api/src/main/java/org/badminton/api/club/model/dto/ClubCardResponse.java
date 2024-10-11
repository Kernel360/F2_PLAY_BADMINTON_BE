package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.enums.MemberTier;

public record ClubCardResponse(
	Long clubId,
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt,
	ClubMemberCountByTier clubMemberCountByTier
) {

	public static ClubCardResponse clubEntityToClubsCardResponse(ClubEntity clubEntity,
		Map<MemberTier, Long> tierCounts) {
		return new ClubCardResponse(clubEntity.getClubId(), clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt(),
			ClubMemberCountByTier.ofClubMemberCountResponse(tierCounts)
		);
	}
}
