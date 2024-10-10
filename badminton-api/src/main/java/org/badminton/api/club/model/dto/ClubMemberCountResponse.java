package org.badminton.api.club.model.dto;

import java.util.Map;

import org.badminton.domain.common.enums.MemberTier;

public record ClubMemberCountResponse(
	Long GoldClubMemberCount,
	Long SilverClubMemberCount,
	Long BronzeClubMemberCount
) {
	public static ClubMemberCountResponse ofClubMemberCountResponse(Map<MemberTier, Long> memberCountByTier) {
		return new ClubMemberCountResponse(memberCountByTier.get(MemberTier.GOLD),
			memberCountByTier.get(MemberTier.SILVER),
			memberCountByTier.get(MemberTier.BRONZE));
	}
}
