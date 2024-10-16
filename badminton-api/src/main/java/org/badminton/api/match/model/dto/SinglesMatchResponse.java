package org.badminton.api.match.model.dto;

import org.badminton.domain.match.model.entity.SinglesMatchEntity;

public record SinglesMatchResponse(
	String participant1Name,
	String participant1Image,
	int participant1WinSetCount,
	String participant2Name,
	String participant2Image,
	int participant2WinSetCount
) {

	public static SinglesMatchResponse fromSinglesMatch(SinglesMatchEntity singlesMatch) {
		return new SinglesMatchResponse(
			singlesMatch.getLeagueParticipant1().getMember().getName(),
			singlesMatch.getLeagueParticipant1().getMember().getProfileImage(),
			singlesMatch.getPlayer1WinSetCount(),
			singlesMatch.getLeagueParticipant2().getMember().getName(),
			singlesMatch.getLeagueParticipant2().getMember().getProfileImage(),
			singlesMatch.getPlayer2WinSetCount()
		);
	}
}
