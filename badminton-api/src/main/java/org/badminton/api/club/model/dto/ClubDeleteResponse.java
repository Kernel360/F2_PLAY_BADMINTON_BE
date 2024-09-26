package org.badminton.api.club.model.dto;

import org.badminton.domain.club.entity.ClubEntity;

public record ClubDeleteResponse(
	Long clubId,
	boolean isClubDeleted
) {

	public static ClubDeleteResponse clubEntityToClubDeleteResponse(ClubEntity clubEntity) {
		return new ClubDeleteResponse(clubEntity.getClubId(), clubEntity.isClubDeleted());
	}
}
