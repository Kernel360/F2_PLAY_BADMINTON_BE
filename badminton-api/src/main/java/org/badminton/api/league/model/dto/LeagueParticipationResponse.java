package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.league.entity.LeagueParticipationEntity;

public record LeagueParticipationResponse(
	Long leagueId,
	Long memberId,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static LeagueParticipationResponse entityToLeagueParticipateResponse(LeagueParticipationEntity entity) {
		return new LeagueParticipationResponse(entity.getLeague().getLeagueId(), entity.getMember().getMemberId(),
			entity.getCreatedAt(), entity.getModifiedAt());
	}
}
