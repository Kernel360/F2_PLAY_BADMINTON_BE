package org.badminton.api.match.model.vo;

import org.badminton.domain.match.model.vo.Team;

import lombok.Getter;

@Getter
public class TeamResponse {
	private Long participant1Id;
	private Long participant2Id;

	public TeamResponse(Long participant1Id, Long participant2Id) {
		this.participant1Id = participant1Id;
		this.participant2Id = participant2Id;
	}

	public static TeamResponse teamToTeamResponse(Team team) {
		return new TeamResponse(
			team.getLeagueParticipant1().getLeagueParticipantId(),
			team.getLeagueParticipant2().getLeagueParticipantId()
		);
	}
}

