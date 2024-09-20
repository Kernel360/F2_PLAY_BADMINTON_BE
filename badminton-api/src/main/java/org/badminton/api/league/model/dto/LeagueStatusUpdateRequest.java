package org.badminton.api.league.model.dto;

import org.badminton.domain.league.entity.LeagueEntity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LeagueStatusUpdateRequest(
	@Schema(description = "특정 경기 아이디", example = "1")
	Long leagueId,

	@Schema(description = "현재 경기 상태", example = "CLOSED")
	String status
) {
	public static LeagueEntity leagueStatusToEntity(
		LeagueEntity leagueEntity,
		LeagueStatusUpdateRequest leagueStatusUpdateRequest) {
		return new LeagueEntity(
			leagueEntity.getLeagueId(),
			leagueEntity.getLeagueName(),
			leagueEntity.getDescription(),
			leagueEntity.getTierLimit(),
			leagueStatusUpdateRequest.status,
			leagueEntity.getMatchType(),
			leagueEntity.getLeagueAt(),
			leagueEntity.getClosedAt(),
			leagueEntity.getPlayerCount(),
			leagueEntity.getMatchingRequirement()
		);
	}
}