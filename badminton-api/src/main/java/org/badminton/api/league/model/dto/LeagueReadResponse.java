package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.enums.LeagueStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record LeagueReadResponse(
	@Schema(description = "경기 아이디", example = "1L")
	Long leagueId,

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	String leagueName,

	@Schema(description = "현재 경기 상태", example = "OPEN")
	LeagueStatus status,

	@Schema(description = "경기 시작 날짜", example = "2024-09-10T15:30:00")
	LocalDateTime leagueAt,

	@Schema(description = "참가 인원", example = "16")
	int playerCount
) {
	public LeagueReadResponse(LeagueEntity entity) {
		this(
			entity.getLeagueId(),
			entity.getLeagueName(),
			entity.getLeagueStatus(),
			entity.getLeagueAt(),
			entity.getPlayerLimitCount()
		);
	}

	public static LeagueReadResponse leagueReadEntityToResponse(LeagueEntity entity) {
		return new LeagueReadResponse(entity);
	}
}
