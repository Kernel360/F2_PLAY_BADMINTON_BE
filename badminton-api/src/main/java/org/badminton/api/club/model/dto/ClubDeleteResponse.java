package org.badminton.api.club.model.dto;

import org.badminton.domain.club.entity.ClubEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClubDeleteResponse {
	private Long clubId;
	private boolean isClubDeleted;

	public static ClubDeleteResponse clubEntityToClubDeleteResponse(ClubEntity clubEntity) {
		return new ClubDeleteResponse(clubEntity.getClubId(), clubEntity.isClubDeleted());
	}
}
