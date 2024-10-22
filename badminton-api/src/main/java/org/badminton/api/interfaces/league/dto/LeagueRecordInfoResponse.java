package org.badminton.api.interfaces.league.dto;

import org.badminton.domain.domain.league.entity.LeagueRecordEntity;

public record LeagueRecordInfoResponse(
        int winCount,
        int loseCount,
        int drawCount,
        int matchCount
) {
    public static LeagueRecordInfoResponse entityToLeagueRecordInfoResponse(LeagueRecordEntity leagueRecordEntity) {
        return new LeagueRecordInfoResponse(
                leagueRecordEntity.getWinCount(),
                leagueRecordEntity.getLoseCount(),
                leagueRecordEntity.getDrawCount(),
                leagueRecordEntity.getMatchCount()
        );
    }

}
