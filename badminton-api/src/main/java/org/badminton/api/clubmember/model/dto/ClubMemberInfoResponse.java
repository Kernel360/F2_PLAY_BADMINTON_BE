package org.badminton.api.clubmember.model.dto;

import org.badminton.api.leaguerecord.dto.LeagueRecordInfoResponse;
import org.badminton.api.member.model.dto.MemberDetailResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.member.entity.MemberEntity;

public record ClubMemberInfoResponse(
	Long memberId,
	String name,
	String email,
	String profileImage,
	Long clubMemberId,
	String clubName,
	ClubMemberRole role,
	LeagueRecordInfoResponse leagueRecordInfoResponse

) implements MemberDetailResponse {
	public static ClubMemberInfoResponse entityToClubMemberInfoResponse(MemberEntity memberEntity,
		ClubMemberEntity clubMemberEntity, LeagueRecordEntity leagueRecordEntity) {
		return new ClubMemberInfoResponse(
			memberEntity.getMemberId(),
			memberEntity.getName(),
			memberEntity.getEmail(),
			memberEntity.getProfileImage(),
			clubMemberEntity.getClubMemberId(),
			clubMemberEntity.getClub().getClubName(),
			clubMemberEntity.getRole(),
			LeagueRecordInfoResponse.entityToLeagueRecordInfoResponse(leagueRecordEntity)
		);
	}

	@Override
	public Long getMemberId() {
		return memberId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getProfileImage() {
		return profileImage;
	}
}
