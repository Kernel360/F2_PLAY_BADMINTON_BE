package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;

public record ClubReadResponse(
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static ClubReadResponse clubEntityToClubReadResponse(ClubEntity clubEntity) {
		return new ClubReadResponse(clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt());
	}
}
