package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.league.entity.LeagueParticipantEntity;

public record LeagueParticipantResponse(
	Long leagueId,
	Long clubMemberId,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static LeagueParticipantResponse entityToLeagueParticipantResponse(LeagueParticipantEntity entity) {
		return new LeagueParticipantResponse(entity.getLeague().getLeagueId(),
			entity.getClubMember().getClubMemberId(),
			entity.getCreatedAt(), entity.getModifiedAt());
	}
}
