package org.badminton.domain.domain.club;

import java.util.Map;

import org.badminton.domain.common.enums.MemberTier;

public record ClubMemberCountByTier(
	Long goldClubMemberCount,
	Long silverClubMemberCount,
	Long bronzeClubMemberCount
) {
	public static ClubMemberCountByTier ofClubMemberCountResponse(Map<MemberTier, Long> memberCountByTier) {
		return new ClubMemberCountByTier(memberCountByTier.get(MemberTier.GOLD),
			memberCountByTier.get(MemberTier.SILVER),
			memberCountByTier.get(MemberTier.BRONZE));
	}
}
