package org.badminton.domain.domain.league;

import org.badminton.domain.domain.league.entity.LeagueRecord;

public record LeagueRecordInfo(
	int winCount,
	int loseCount,
	int drawCount,
	int matchCount
) {
	public static LeagueRecordInfo toLeagueRecordInfo(LeagueRecord league) {
		return new LeagueRecordInfo(league.getWinCount(), league.getLoseCount(), league.getDrawCount(), league.getMatchCount());
	}
}
