package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubReadResponse {

	private String clubName;
	private String clubDescription;
	private String clubImage;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static ClubReadResponse clubEntityToClubReadResponse(ClubEntity clubEntity) {
		return new ClubReadResponse(clubEntity.getClubName(), clubEntity.getClubDescription(),
			clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt());
	}
}
