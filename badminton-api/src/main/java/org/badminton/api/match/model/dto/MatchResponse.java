package org.badminton.api.match.model.dto;

import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;

public record MatchResponse(
	Long matchId,
	Long leagueId,
	MatchType matchType,
	SinglesMatchResponse singlesMatch,
	DoublesMatchResponse doublesMatch
) {

	public static MatchResponse entityToSinglesMatchResponse(SinglesMatchEntity singlesMatch) {
		return new MatchResponse(singlesMatch.getSinglesMatchId(), singlesMatch.getLeague().getLeagueId(),
			MatchType.SINGLE,
			new SinglesMatchResponse(
				singlesMatch.getLeagueParticipant1().getLeagueParticipantId(),
				singlesMatch.getLeagueParticipant2().getLeagueParticipantId()),
			null
		);
	}

	public static MatchResponse entityToDoublesMatchResponse(DoublesMatchEntity doublesMatch) {
		return new MatchResponse(
			doublesMatch.getDoublesMatchId(),
			doublesMatch.getLeague().getLeagueId(),
			MatchType.DOUBLES,
			null,
			new DoublesMatchResponse(
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam1()),
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam2())
			)
		);
	}
}