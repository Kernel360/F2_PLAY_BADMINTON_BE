package org.badminton.api.match.model.dto;

import org.badminton.domain.match.model.vo.Team;

public record TeamResponse(
	String participant1Name,
	String participant1Image,
	String participant2Name,
	String participant2Image
) {

	public static TeamResponse teamToTeamResponse(Team team) {
		return new TeamResponse(
			team.getLeagueParticipant1().getMember().getName(),
			team.getLeagueParticipant1().getMember().getProfileImage(),
			team.getLeagueParticipant2().getMember().getName(),
			team.getLeagueParticipant2().getMember().getProfileImage()
		);
	}
}

