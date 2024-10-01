package org.badminton.api.match.model.dto;

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
	private Long participant1Id;
	private Long participant2Id;
	private TeamResponse team1;
	private TeamResponse team2;

	public MatchResponse(Long matchId, Long leagueId, MatchType matchType, Long participant1Id, Long participant2Id) {
		this.matchId = matchId;
		this.leagueId = leagueId;
		this.matchType = matchType;
		this.participant1Id = participant1Id;
		this.participant2Id = participant2Id;
	}

	public MatchResponse(Long matchId, Long leagueId, MatchType matchType, TeamResponse team1, TeamResponse team2) {
		this.matchId = matchId;
		this.leagueId = leagueId;
		this.matchType = matchType;
		this.team1 = team1;
		this.team2 = team2;
	}

	public static MatchResponse entityToSinglesMatchResponse(SinglesMatchEntity singlesMatch) {
		return new MatchResponse(singlesMatch.getSinglesMatchId(), singlesMatch.getLeague().getLeagueId(),
			singlesMatch.getLeague().getMatchType(), singlesMatch.getLeagueParticipant1().getLeagueParticipantId(),
			singlesMatch.getLeagueParticipant2().getLeagueParticipantId());
	}

	public static MatchResponse entityToDoublesMatchResponse(DoublesMatchEntity doublesMatch) {
		return new MatchResponse(
			doublesMatch.getDoublesMatchId(),
			doublesMatch.getLeague().getLeagueId(),
			doublesMatch.getLeague().getMatchType(),
			TeamResponse.teamToTeamResponse(doublesMatch.getTeam1()),
			TeamResponse.teamToTeamResponse(doublesMatch.getTeam2())
		);
	}
}