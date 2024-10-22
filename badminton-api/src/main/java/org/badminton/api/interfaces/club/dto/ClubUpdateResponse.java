package org.badminton.api.interfaces.club.dto;

import java.time.LocalDateTime;
import org.badminton.domain.domain.club.entity.ClubEntity;

public record ClubUpdateResponse(
        Long clubId,
        String clubName,
        String clubDescription,
        String clubImage,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ClubUpdateResponse fromClubEntity(ClubEntity clubEntity) {
        return new ClubUpdateResponse(
                clubEntity.getClubId(),
                clubEntity.getClubName(),
                clubEntity.getClubDescription(),
                clubEntity.getClubImage(),
                clubEntity.getCreatedAt(),
                clubEntity.getModifiedAt()
        );
    }
}
