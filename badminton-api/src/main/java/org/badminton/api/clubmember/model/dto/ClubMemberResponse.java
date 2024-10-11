package org.badminton.api.clubmember.model.dto;

import org.badminton.api.leaguerecord.dto.LeagueRecordInfoResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;

public record ClubMemberResponse(
	Long clubMemberId,
	String image,
	String name,
	ClubMemberRole role,
	LeagueRecordInfoResponse leagueRecordInfoResponse
) {

	public static ClubMemberResponse entityToClubMemberResponse(ClubMemberEntity clubMember) {
		return new ClubMemberResponse(clubMember.getClubMemberId(),clubMember.getMember().getProfileImage(), clubMember.getMember().getName(),
			clubMember.getRole(), LeagueRecordInfoResponse.entityToLeagueRecordInfoResponse(
			clubMember.getLeagueRecord()));
	}
}
