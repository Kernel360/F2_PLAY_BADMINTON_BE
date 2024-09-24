package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;

public record ClubCreateResponse(
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static ClubCreateResponse clubEntityToClubCreateResponse(ClubEntity clubEntity) {
		return new ClubCreateResponse(clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt());
	}
}
