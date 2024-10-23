package org.badminton.domain.domain.club;

import java.util.Map;

import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ClubPage {
	private final Page<Club> club;

	public Page<ClubCardInfo> clubToPageCardInfo() {

		return this.club.map(club -> {

			Map<MemberTier, Long> tierCounts = club.getClubMemberCountByTier();

			return ClubCardInfo.clubEntityToClubsCardResponse(club, tierCounts);

		});

	}

}

