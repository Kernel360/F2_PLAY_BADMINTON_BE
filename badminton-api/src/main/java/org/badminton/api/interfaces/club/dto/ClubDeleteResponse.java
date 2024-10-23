package org.badminton.api.interfaces.club.dto;

import org.badminton.domain.domain.club.entity.ClubEntity;

public record ClubDeleteResponse(
        Long clubId,
        boolean isClubDeleted
) {

    public static ClubDeleteResponse clubEntityToClubDeleteResponse(ClubEntity clubEntity) {
        return new ClubDeleteResponse(clubEntity.getClubId(), clubEntity.isClubDeleted());
    }
}
