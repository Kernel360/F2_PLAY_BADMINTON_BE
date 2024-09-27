package org.badminton.api.league.model.dto;

import org.badminton.domain.league.enums.LeagueStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record LeagueStatusUpdateRequest(
	@Schema(description = "특정 경기 아이디", example = "1")
	Long leagueId,

	@Schema(description = "현재 경기 상태", example = "CLOSED")
	LeagueStatus status
) {
}
