package org.badminton.api.interfaces.member.dto;

import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;

public record MemberIsClubMemberResponse(
        boolean isClubMember,
        ClubMemberRole role,
        Long clubId
) {
}
