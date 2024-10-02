package org.badminton.api.match.model.vo;

import org.badminton.domain.match.model.vo.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamResponse {
	private Long participant1Id;
	private Long participant2Id;

	public static TeamResponse teamToTeamResponse(Team team) {
		return new TeamResponse(
			team.getLeagueParticipant1().getLeagueParticipantId(),
			team.getLeagueParticipant2().getLeagueParticipantId()
		);
	}
}

