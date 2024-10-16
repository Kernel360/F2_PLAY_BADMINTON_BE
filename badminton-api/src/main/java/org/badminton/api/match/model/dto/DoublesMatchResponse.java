package org.badminton.api.match.model.dto;

import org.badminton.domain.match.model.entity.DoublesMatchEntity;

public record DoublesMatchResponse(
	TeamResponse team1,
	int team1WinSetCount,
	TeamResponse team2,
	int team2WinSetCount
) {

	public static DoublesMatchResponse fromDoublesMatchEntity(DoublesMatchEntity doublesMatch) {
		return new DoublesMatchResponse(TeamResponse.teamToTeamResponse(doublesMatch.getTeam1()),
			doublesMatch.getTeam1WinSetCount(),
			TeamResponse.teamToTeamResponse(doublesMatch.getTeam2()),
			doublesMatch.getTeam2WinSetCount()
		);
	}
}
