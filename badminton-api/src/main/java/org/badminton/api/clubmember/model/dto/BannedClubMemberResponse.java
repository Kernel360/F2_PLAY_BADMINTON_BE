package org.badminton.api.clubmember.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.clubmember.entity.BannedClubMemberEntity;
import org.badminton.domain.clubmember.entity.BannedType;

public record BannedClubMemberResponse(
	BannedType bannedType,
	String bannedReason,
	Long clubMemberId,
	boolean isActive,
	LocalDateTime endDate
) {
	public static BannedClubMemberResponse entityToBannedClubMemberResponse(BannedClubMemberEntity bannedClubMemberEntity) {
		return new BannedClubMemberResponse(
			bannedClubMemberEntity.getBannedType(),
			bannedClubMemberEntity.getBannedReason(),
			bannedClubMemberEntity.getClubMember().getClubMemberId(),
			bannedClubMemberEntity.isActive(),
			bannedClubMemberEntity.getEndDate()
		);
	}
}
