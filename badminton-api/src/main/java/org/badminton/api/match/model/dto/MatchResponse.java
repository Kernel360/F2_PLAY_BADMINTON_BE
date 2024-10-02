package org.badminton.api.match.model.dto;

import org.badminton.api.match.model.vo.DoublesMatchResponse;
import org.badminton.api.match.model.vo.SinglesMatchResponse;
import org.badminton.api.match.model.vo.TeamResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;

import lombok.Getter;

@Getter
public class MatchResponse {
	private final Long matchId;
	private final Long leagueId;
	private final MatchType matchType;
	private SinglesMatchResponse singlesMatch;
	private DoublesMatchResponse doublesMatch;

	public MatchResponse(Long matchId, Long leagueId, MatchType matchType, SinglesMatchResponse singlesMatch) {
		this.matchId = matchId;
		this.leagueId = leagueId;
		this.matchType = matchType;
		this.singlesMatch = singlesMatch;
	}

	public MatchResponse(Long matchId, Long leagueId, MatchType matchType, DoublesMatchResponse doublesMatch) {
		this.matchId = matchId;
		this.leagueId = leagueId;
		this.matchType = matchType;
		this.doublesMatch = doublesMatch;
	}

	public static MatchResponse entityToSinglesMatchResponse(SinglesMatchEntity singlesMatch) {
		return new MatchResponse(singlesMatch.getSinglesMatchId(), singlesMatch.getLeague().getLeagueId(),
			MatchType.SINGLE,
			new SinglesMatchResponse(
				singlesMatch.getLeagueParticipant1().getLeagueParticipantId(),
				singlesMatch.getLeagueParticipant2().getLeagueParticipantId())
		);
	}

	public static MatchResponse entityToDoublesMatchResponse(DoublesMatchEntity doublesMatch) {
		return new MatchResponse(
			doublesMatch.getDoublesMatchId(),
			doublesMatch.getLeague().getLeagueId(),
			MatchType.DOUBLES,
			new DoublesMatchResponse(
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam1()),
				TeamResponse.teamToTeamResponse(doublesMatch.getTeam2())
			)
		);
	}
}