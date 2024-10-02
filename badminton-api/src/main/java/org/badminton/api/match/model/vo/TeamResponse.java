package org.badminton.api.match.model.vo;

import org.badminton.domain.match.model.vo.Team;

public record TeamResponse(
	Long participant1Id,
	Long participant2Id
) {

	public static TeamResponse teamToTeamResponse(Team team) {
		return new TeamResponse(
			team.getLeagueParticipant1().getLeagueParticipantId(),
			team.getLeagueParticipant2().getLeagueParticipantId()
		);
	}
}

