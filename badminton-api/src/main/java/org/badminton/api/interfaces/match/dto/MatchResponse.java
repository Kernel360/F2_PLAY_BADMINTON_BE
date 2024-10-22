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
        return new MatchResponse(matchInfo.getMatchId(), matchInfo.getLeagueId(), matchInfo.getMatchType(),
                SinglesMatchResponse.fromSinglesMatchInfo(matchInfo.getSinglesMatchInfo()),
                DoublesMatchResponse.fromDoublesMatchInfo(matchInfo.getDoublesMatchInfo()),
                matchInfo.getMatchStatus());
    }
}