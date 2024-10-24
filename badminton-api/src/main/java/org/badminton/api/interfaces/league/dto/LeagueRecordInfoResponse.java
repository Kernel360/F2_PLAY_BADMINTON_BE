package org.badminton.api.interfaces.league.dto;

import org.badminton.domain.domain.league.entity.LeagueRecord;

public record LeagueRecordInfoResponse(
        int winCount,
        int loseCount,
        int drawCount,
        int matchCount
) {
    public static LeagueRecordInfoResponse entityToLeagueRecordInfoResponse(LeagueRecord leagueRecord) {
        return new LeagueRecordInfoResponse(
                leagueRecord.getWinCount(),
                leagueRecord.getLoseCount(),
                leagueRecord.getDrawCount(),
                leagueRecord.getMatchCount()
        );
    }

}
