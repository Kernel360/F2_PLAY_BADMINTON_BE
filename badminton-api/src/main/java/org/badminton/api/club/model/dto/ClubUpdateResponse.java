package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;

public record ClubUpdateResponse(
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static ClubUpdateResponse clubEntityToClubUpdateResponse(ClubEntity clubEntity) {
		return new ClubUpdateResponse(
			clubEntity.getClubName(),
			clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt()
		);
	}
}
