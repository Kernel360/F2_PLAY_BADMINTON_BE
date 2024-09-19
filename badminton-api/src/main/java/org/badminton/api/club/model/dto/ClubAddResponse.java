package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubAddResponse {
	private String clubName;
	private String clubDescription;
	private String clubImage;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static ClubAddResponse clubEntityToClubAddResponse(ClubEntity clubEntity) {
		return new ClubAddResponse(clubEntity.getClubName(), clubEntity.getClubDescription(), clubEntity.getClubImage(),
			clubEntity.getCreatedAt(),
			clubEntity.getModifiedAt());
	}
}
