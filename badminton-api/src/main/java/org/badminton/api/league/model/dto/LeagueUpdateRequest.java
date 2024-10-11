package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchGenerationType;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.enums.MemberTier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record LeagueUpdateRequest(

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	String leagueName,

	@Schema(description = "경기 설명", example = "이 경기는 지역 예선 경기입니다.")
	String description,

	@Schema(description = "경기 장소", example = "성동구 서울숲 체육센터")
	String leagueLocation,

	@Pattern(regexp = "GOLD|SILVER|BRONZE", message = "리그 상태 값이 올바르지 않습니다.")
	@Schema(description = "최소 티어", example = "GOLD")
	MemberTier tierLimit,

	@Pattern(regexp = "SINGLE|DOUBLES", message = "경기 방식 값이 올바르지 않습니다.")
	@Schema(description = "경기 방식", example = "SINGLES")
	MatchType matchType,

	@Schema(description = "경기 시작 날짜", example = "2024-09-10T15:30:00")
	LocalDateTime leagueAt,

	@Schema(description = "모집 마감 날짜", example = "2024-09-08T23:59:59")
	LocalDateTime closedAt,

	@Schema(description = "참가 인원", example = "16")
	int playerCount,

	@Schema(description = "매칭 조건", example = "TIER")
	MatchGenerationType matchGenerationType

) {
}
