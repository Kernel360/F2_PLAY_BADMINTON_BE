package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.enums.MemberTier;

public record ClubsReadResponse(
	Long clubId,
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt,
	Map<MemberTier, Long> tierCounts
) {

	public static ClubsReadResponse clubEntityToClubsReadResponse(ClubEntity clubEntity,
		Map<MemberTier, Long> tierCounts) {
		return new ClubsReadResponse(clubEntity.getClubId(),clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt(),
			tierCounts
		);
	}
}
