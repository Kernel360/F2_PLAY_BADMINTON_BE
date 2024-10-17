package org.badminton.api.clubmember.model.dto;

public record ClubMemberWithdrawResponse(
	Long clubId,
	Long clubMemberId,
	boolean isDeleted
) {
}
