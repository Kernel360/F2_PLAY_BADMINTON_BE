package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.enums.LeagueStatus;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LeagueCreateRequest(

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	String leagueName,

	@Schema(description = "경기 설명", example = "이 경기는 지역 예선 경기입니다.")
	String description,

	@Schema(description = "최소 티어", example = "GOLD")
	MemberTier tierLimit,

	@Schema(description = "현재 경기 상태", example = "OPEN")
	LeagueStatus status,

	@Schema(description = "경기 방식", example = "SINGLE")
	MatchType matchType,

	@Schema(description = "경기 시작 날짜", example = "2024-09-10T15:30:00")
	LocalDateTime leagueAt,

	@Schema(description = "모집 마감 날짜", example = "2024-09-08T23:59:59")
	LocalDateTime closedAt,

	@Schema(description = "참가 인원", example = "16")
	Long playerCount,

	@Schema(description = "매칭 조건", example = "TIER")
	String matchingRequirement

) {
	public LeagueEntity leagueCreateRequestToEntity() {
		return new LeagueEntity(this.leagueName,
			this.description, this.leagueAt, this.tierLimit, this.closedAt, this.status, this.playerCount,
			this.matchType, this.matchingRequirement);
	}
}
