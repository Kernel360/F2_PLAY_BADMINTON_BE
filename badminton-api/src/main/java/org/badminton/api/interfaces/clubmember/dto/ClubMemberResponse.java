package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.api.interfaces.league.dto.LeagueRecordInfoResponse;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.common.enums.MemberTier;

public record ClubMemberResponse(
        Long clubMemberId,
        String image,
        String name,
        ClubMemberRole role,
        MemberTier tier,
        LeagueRecordInfoResponse leagueRecordInfoResponse
) {

    public static ClubMemberResponse entityToClubMemberResponse(ClubMemberEntity clubMember) {
        return new ClubMemberResponse(clubMember.getClubMemberId(), clubMember.getMember().getProfileImage(),
                clubMember.getMember().getName(),
                clubMember.getRole(), clubMember.getTier(), LeagueRecordInfoResponse.entityToLeagueRecordInfoResponse(
                clubMember.getLeagueRecord()));
    }
}
