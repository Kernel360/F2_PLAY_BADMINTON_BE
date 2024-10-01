package org.badminton.api.match.model.dto;

import org.badminton.domain.match.entity.SinglesMatchEntity;

public class SinglesMatchResponse extends MatchResponse {
	private Long leagueParticipant1Id;
	private Long leagueParticipant2Id;

	public SinglesMatchResponse(SinglesMatchEntity entity) {
		super(entity.getSinglesMatchId(), entity.getLeague().getLeagueId(), entity.getLeague().getMatchType(),
			entity.getCreatedAt(), entity.getModifiedAt());
		this.leagueParticipant1Id = entity.getLeagueParticipant1().getLeagueParticipantId();
		this.leagueParticipant2Id = entity.getLeagueParticipant2().getLeagueParticipantId();
	}
}
