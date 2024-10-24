package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.api.interfaces.league.dto.LeagueRecordInfoResponse;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.info.ClubMemberInfo;

public record ClubMemberResponse(
        Long clubMemberId,
        String image,
        String name,
        ClubMember.ClubMemberRole role
) {

    public static ClubMemberResponse entityToClubMemberResponse(ClubMemberInfo info) {
        return new ClubMemberResponse(
            info.clubMemberId(),
            info.image(),
            info.name(),
            info.role()
        );
    }
}
