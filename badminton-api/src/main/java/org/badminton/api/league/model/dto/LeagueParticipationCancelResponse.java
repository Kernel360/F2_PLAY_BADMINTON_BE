package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.league.entity.LeagueParticipationEntity;

public record LeagueParticipationCancelResponse(

	Long leagueId,
	Long memberId,
	LocalDateTime createdAt,
	LocalDateTime deletedAt
) {

	public static LeagueParticipationCancelResponse entityToLeagueParticipateCancelResponse(
		LeagueParticipationEntity entity) {

		return new LeagueParticipationCancelResponse(entity.getLeague().getLeagueId(), entity.getMember().getMemberId(),
			entity.getCreatedAt(), entity.getModifiedAt());

	}

}
