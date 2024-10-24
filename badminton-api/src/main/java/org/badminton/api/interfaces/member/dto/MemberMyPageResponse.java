package org.badminton.api.interfaces.member.dto;

import org.badminton.api.interfaces.league.dto.LeagueRecordInfo;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.info.ClubMemberMyPageInfo;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.domain.member.entity.Member;

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

	@Schema(description = "ClubMember information")
	ClubMemberMyPageInfo clubMemberMyPageInfo,

	@Schema(description = "League record information")
	LeagueRecordInfo leagueRecordInfo
) {
	public static MemberMyPageResponse fromMemberEntityAndClubMemberEntity(Member member,
		ClubMember clubMember,
		LeagueRecord leagueRecord) {
		return new MemberMyPageResponse(
			member.getId(),
			member.getName(),
			member.getEmail(),
			member.getProfileImage(),
			ClubMemberMyPageInfo.from(clubMember),
			LeagueRecordInfo.from(leagueRecord)
		);
	}

	public static MemberMyPageResponse fromMemberEntity(Member member) {
		return new MemberMyPageResponse(
			member.getId(),
			member.getName(),
			member.getEmail(),
			member.getProfileImage(),
			null,
			null
		);
	}
}