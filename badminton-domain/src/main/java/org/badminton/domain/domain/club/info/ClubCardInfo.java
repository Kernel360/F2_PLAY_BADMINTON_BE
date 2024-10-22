package org.badminton.domain.domain.club.info;

import java.time.LocalDateTime;
import java.util.Map;

import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.club.ClubMemberCountByTier;

public record ClubCardInfo(
	Long clubId,
	String clubName,
	String clubDescription,
	String clubImage,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt,
	ClubMemberCountByTier clubMemberCountByTier
) {
	public static ClubCardInfo clubEntityToClubsCardResponse(Club club,
		Map<MemberTier, Long> tierCounts) {
		return new ClubCardInfo(club.getClubId(), club.getClubName(), club.getClubDescription(),
			club.getClubImage(),
			club.getCreatedAt(),
			club.getModifiedAt(),
			ClubMemberCountByTier.ofClubMemberCountResponse(tierCounts)
		);
	}
}
