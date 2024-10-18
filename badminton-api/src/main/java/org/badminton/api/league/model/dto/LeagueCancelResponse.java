package org.badminton.api.league.model.dto;

import org.badminton.domain.league.enums.LeagueStatus;

public record LeagueCancelResponse(
	Long leagueId,
	LeagueStatus leagueStatus
) {
}
