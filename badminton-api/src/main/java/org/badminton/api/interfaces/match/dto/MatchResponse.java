package org.badminton.api.interfaces.match.dto;

import org.badminton.domain.common.enums.MatchStatus;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.domain.match.info.MatchInfo;

public record MatchResponse(
        Long matchId,
        Long leagueId,
        MatchType matchType,
        SinglesMatchResponse singlesMatch,
        DoublesMatchResponse doublesMatch,
        MatchStatus matchStatus
) {

    public static MatchResponse fromMatchInfo(MatchInfo.Main matchInfo) {
        return new MatchResponse(
                matchInfo.matchId(),
                matchInfo.leagueId(),
                matchInfo.matchType(),
                matchInfo.matchType() == MatchType.SINGLES
                        ? SinglesMatchResponse.fromSinglesMatchInfo(matchInfo.singlesMatchInfo())
                        : null,
                matchInfo.matchType() == MatchType.DOUBLES
                        ? DoublesMatchResponse.fromDoublesMatchInfo(matchInfo.doublesMatchInfo())
                        : null,
                matchInfo.matchStatus()
        );
    }
}