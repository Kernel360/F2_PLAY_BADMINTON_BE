package org.badminton.api.match.model.dto;

import org.badminton.domain.common.enums.MatchResult;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.vo.Team;

public record DoublesMatchResultResponse(
	TeamResultResponse currentTeam,
	TeamResultResponse opponentTeam,
	MatchResult currentTeamResult,
	MatchResult opponentTeamResult
) {
	public static DoublesMatchResultResponse fromDoublesMatchEntity(DoublesMatchEntity doublesMatch, boolean isTeam1, Long clubMemberId) {
		Team team1 = doublesMatch.getTeam1();
		Team team2 = doublesMatch.getTeam2();

		boolean isPlayer1InTeam1 = team1.getLeagueParticipant1().getClubMember().getClubMemberId().equals(clubMemberId);
		boolean isPlayer2InTeam1 = team1.getLeagueParticipant2().getClubMember().getClubMemberId().equals(clubMemberId);

		TeamResultResponse currentTeam;
		TeamResultResponse opponentTeam;
		MatchResult currentTeamResult;
		MatchResult opponentTeamResult;

		if (isPlayer1InTeam1 || isPlayer2InTeam1) {
			currentTeam = TeamResultResponse.fromTeam(team1, clubMemberId);
			opponentTeam = TeamResultResponse.fromTeam(team2, null);
			currentTeamResult = doublesMatch.getTeam1MatchResult();
			opponentTeamResult = doublesMatch.getTeam2MatchResult();
		} else {
			currentTeam = TeamResultResponse.fromTeam(team2, clubMemberId);
			opponentTeam = TeamResultResponse.fromTeam(team1, null);
			currentTeamResult = doublesMatch.getTeam2MatchResult();
			opponentTeamResult = doublesMatch.getTeam1MatchResult();
		}

		return new DoublesMatchResultResponse(currentTeam, opponentTeam, currentTeamResult, opponentTeamResult);
	}
}
