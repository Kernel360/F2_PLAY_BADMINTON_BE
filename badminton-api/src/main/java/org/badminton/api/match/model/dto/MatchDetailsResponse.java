package org.badminton.api.match.model.dto;

import java.util.List;

import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;

public record MatchDetailsResponse(
	Long matchId,
	Long leagueId,
	MatchType matchType,
	SinglesMatchResponse singlesMatch,
	DoublesMatchResponse doublesMatch,
	List<SinglesSetResponse> singlesSets,
	List<DoublesSetResponse> doublesSets
) {

	public static MatchDetailsResponse entityToSinglesMatchDetailsResponse(SinglesMatchEntity singlesMatch) {

		return new MatchDetailsResponse(singlesMatch.getSinglesMatchId(), singlesMatch.getLeague().getLeagueId(),
			MatchType.SINGLES,
			new SinglesMatchResponse(
				singlesMatch.getLeagueParticipant1().getLeagueParticipantId(),
				singlesMatch.getLeagueParticipant2().getLeagueParticipantId()),
			null,
			SinglesSetResponse.entityToSinglesSetResponse(singlesMatch.getSinglesSets()),
			null
		);
	}

	public static MatchDetailsResponse entityToDoublesMatchDetailsResponse(DoublesMatchEntity doublesMatch) {

		return new MatchDetailsResponse(
			doublesMatch.getDoublesMatchId(),
			doublesMatch.getLeague().getLeagueId(),
			MatchType.DOUBLES,
			null,
			new DoublesMatchResponse(
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam1()),
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam2())
			),
			null,
			DoublesSetResponse.entityToDoublesSetResponse(doublesMatch.getDoublesSets())
		);

	}

}
