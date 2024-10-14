package org.badminton.api.member.model.dto;

import org.badminton.api.leaguerecord.dto.LeagueRecordInfoResponse;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Unified member response")
public record MemberMyPageResponse(
	@Schema(description = "Member ID", example = "1")
	Long memberId,

	@Schema(description = "Member name", example = "김철수")
	String name,

	@Schema(description = "Email", example = "example@email.com")
	String email,

	@Schema(description = "Profile image URL", example = "https://example.com/profile.jpg")
	String profileImage,

	@Schema(description = "Club ID", example = "1")
	Long clubId,

	@Schema(description = "Club member ID", example = "1")
	Long clubMemberId,

	@Schema(description = "Club name", example = "배드민턴 동호회")
	String clubName,

	@Schema(description = "Member role", example = "ROLE_USER")
	ClubMemberRole role,

	@Schema(description = "ClubMember Tier", example = "GOLD")
	MemberTier tier,

	@Schema(description = "League record information")
	LeagueRecordInfoResponse leagueRecordInfo
) {
	public static MemberMyPageResponse entityToMyPageResponse(MemberEntity memberEntity, ClubMemberEntity clubMemberEntity, LeagueRecordEntity leagueRecordEntity) {
		return new MemberMyPageResponse(
			memberEntity.getMemberId(),
			memberEntity.getName(),
			memberEntity.getEmail(),
			memberEntity.getProfileImage(),
			clubMemberEntity != null ? clubMemberEntity.getClub().getClubId() : null,
			clubMemberEntity != null ? clubMemberEntity.getClubMemberId() : null,
			clubMemberEntity != null ? clubMemberEntity.getClub().getClubName() : null,
			clubMemberEntity != null ? clubMemberEntity.getRole() : null,
			clubMemberEntity != null ? clubMemberEntity.getTier() : null,
			leagueRecordEntity != null ? LeagueRecordInfoResponse.entityToLeagueRecordInfoResponse(leagueRecordEntity) : null
		);
	}
}