package org.badminton.api.match.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchStatus;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;

public record MatchResultResponse(
	MatchType matchType,
	SinglesMatchResultResponse singlesMatch,
	DoublesMatchResultResponse doublesMatch,
	MatchStatus matchStatus,
	LocalDateTime leagueAt
) {
	public static MatchResultResponse fromSinglesMatchEntity(SinglesMatchEntity singlesMatch, Long clubMemberId) {
		boolean isPlayer1 = singlesMatch.getLeagueParticipant1().getClubMember().getClubMemberId().equals(clubMemberId);
		return new MatchResultResponse(
			MatchType.SINGLES,
			SinglesMatchResultResponse.fromSinglesMatch(singlesMatch, isPlayer1, clubMemberId),
			null,
			singlesMatch.getMatchStatus()
			,singlesMatch.getLeague().getLeagueAt()
		);
	}

	public static MatchResultResponse fromDoublesMatchEntity(DoublesMatchEntity doublesMatch, Long clubMemberId) {
		boolean isTeam1 = isPlayerInTeam1(doublesMatch, clubMemberId);
		return new MatchResultResponse(
			MatchType.DOUBLES,
			null,
			DoublesMatchResultResponse.fromDoublesMatchEntity(doublesMatch, isTeam1, clubMemberId),
			doublesMatch.getMatchStatus(),
			doublesMatch.getLeague().getLeagueAt()
		);
	}

	private static boolean isPlayerInTeam1(DoublesMatchEntity match, Long clubMemberId) {
		return match.getTeam1().getLeagueParticipant1().getClubMember().getClubMemberId().equals(clubMemberId) ||
			match.getTeam1().getLeagueParticipant2().getClubMember().getClubMemberId().equals(clubMemberId);
	}
}
