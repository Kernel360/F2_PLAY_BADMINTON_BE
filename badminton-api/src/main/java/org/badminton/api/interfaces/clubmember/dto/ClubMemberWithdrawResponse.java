package org.badminton.api.interfaces.clubmember.dto;

public record ClubMemberWithdrawResponse(
        Long clubId,
        Long clubMemberId,
        boolean isDeleted
) {
}
