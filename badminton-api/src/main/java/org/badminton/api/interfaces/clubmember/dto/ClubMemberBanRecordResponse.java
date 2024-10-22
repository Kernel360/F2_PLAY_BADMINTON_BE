package org.badminton.api.interfaces.clubmember.dto;

import java.time.LocalDateTime;

import org.badminton.domain.domain.clubmember.entity.BannedType;
import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;

public record ClubMemberBanRecordResponse(
        BannedType bannedType,
        String bannedReason,
        Long clubMemberId,
        boolean isActive,
        LocalDateTime endDate
) {
    public static ClubMemberBanRecordResponse entityToClubMemberBanRecordResponse(
            ClubMemberBanRecordEntity clubMemberBanRecordEntity) {
        return new ClubMemberBanRecordResponse(
                clubMemberBanRecordEntity.getBannedType(),
                clubMemberBanRecordEntity.getBannedReason(),
                clubMemberBanRecordEntity.getClubMember().getClubMemberId(),
                clubMemberBanRecordEntity.isActive(),
                clubMemberBanRecordEntity.getEndDate()
        );
    }
}
