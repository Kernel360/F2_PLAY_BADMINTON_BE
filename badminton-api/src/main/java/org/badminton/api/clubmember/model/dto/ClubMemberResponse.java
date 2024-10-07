package org.badminton.api.clubmember.model.dto;

import org.badminton.api.leaguerecord.dto.LeagueRecordInfoResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public record ClubMemberResponse(
	String image,
	String name,
	LeagueRecordInfoResponse leagueRecordInfoResponse
) {

	public static ClubMemberResponse entityToClubMemberResponse(ClubMemberEntity clubMember) {
		return new ClubMemberResponse(clubMember.getMember().getProfileImage(), clubMember.getMember().getName(),
			LeagueRecordInfoResponse.entityToLeagueRecordInfoResponse(clubMember.getLeagueRecord()));
	}
}
