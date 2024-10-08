package org.badminton.api.clubmember.model.dto;

import org.badminton.api.leaguerecord.dto.LeagueRecordInfoResponse;
import org.badminton.api.member.model.dto.MemberDetailResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.member.entity.MemberEntity;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubMemberInfoResponse(
	@Schema(description = "회원 ID", example = "1")
	Long memberId,
	@Schema(description = "회원 이름", example = "김철수")
	String name,
	@Schema(description = "이메일", example = "sunn10189@gmail.com")
	String email,
	@Schema(description = "프로필 이미지", example = "https://lh3.googleusercontent.com/a/ACg8ocI_eGCDYzQe6vX3e4wCYpm6syVZ-UnGg9TZtm6S1U2K04PiAw=s96-c")
	String profileImage,
	Long clubId,
	@Schema(description = "동호회 회원 ID", example = "1")
	Long clubMemberId,
	@Schema(description = "동호회 이름", example = "동호회이름")
	String clubName,
	@Schema(description = "역할", example = "ROLE_USER")
	ClubMemberRole role,
	@Schema(description = "전적", example = "\"league_record_info_response\": {\n"
		+ "    \"win_count\": 0,\n"
		+ "    \"lose_count\": 0,\n"
		+ "    \"draw_count\": 0,\n"
		+ "    \"match_count\": 0,\n"
		+ "    \"tier\": \"BRONZE\"\n"
		+ "  }")
	LeagueRecordInfoResponse leagueRecordInfoResponse

) implements MemberDetailResponse {
	public static ClubMemberInfoResponse entityToClubMemberInfoResponse(MemberEntity memberEntity,
		ClubMemberEntity clubMemberEntity, LeagueRecordEntity leagueRecordEntity) {
		return new ClubMemberInfoResponse(
			memberEntity.getMemberId(),
			memberEntity.getName(),
			memberEntity.getEmail(),
			memberEntity.getProfileImage(),
			clubMemberEntity.getClub().getClubId(),
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
