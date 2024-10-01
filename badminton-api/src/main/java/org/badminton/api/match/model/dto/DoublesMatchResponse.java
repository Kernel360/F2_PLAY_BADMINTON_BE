package org.badminton.api.match.model.dto;

import org.badminton.domain.match.entity.DoublesMatchEntity;

public class DoublesMatchResponse extends MatchResponse {

	private Long leagueParticipant1Id;
	private Long leagueParticipant2Id;
	private Long leagueParticipant3Id;
	private Long leagueParticipant4Id;

	public DoublesMatchResponse(DoublesMatchEntity entity) {
		super(entity.getDoublesMatchId(), entity.getLeague().getLeagueId(), entity.getLeague().getMatchType(),
			entity.getCreatedAt(), entity.getModifiedAt());
		this.leagueParticipant1Id = entity.getLeagueParticipant1().getLeagueParticipantId();
		this.leagueParticipant2Id = entity.getLeagueParticipant2().getLeagueParticipantId();
		this.leagueParticipant3Id = entity.getLeagueParticipant3().getLeagueParticipantId();
		this.leagueParticipant4Id = entity.getLeagueParticipant4().getLeagueParticipantId();
	}
}
