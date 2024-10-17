package org.badminton.api.clubmember.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.clubmember.entity.BannedType;

public record clubMemberBanRecordResponse(
	BannedType bannedType,
	String bannedReason,
	Long clubMemberId,
	boolean isActive,
	LocalDateTime endDate
) {
	public static clubMemberBanRecordResponse entityToClubMemberBanRecordResponse(
		ClubMemberBanRecordEntity clubMemberBanRecordEntity) {
		return new clubMemberBanRecordResponse(
			clubMemberBanRecordEntity.getBannedType(),
			clubMemberBanRecordEntity.getBannedReason(),
			clubMemberBanRecordEntity.getClubMember().getClubMemberId(),
			clubMemberBanRecordEntity.isActive(),
			clubMemberBanRecordEntity.getEndDate()
		);
	}
}
