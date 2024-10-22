package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public record ClubMemberJoinResponse(
        Long clubMemberId,
        String role
) {
    public static ClubMemberJoinResponse clubMemberEntityToClubMemberJoinResponse(
            ClubMemberEntity clubMemberEntity) {
        return new ClubMemberJoinResponse(
                clubMemberEntity.getClubMemberId(),
                clubMemberEntity.getRole().name()
        );

    }
}
