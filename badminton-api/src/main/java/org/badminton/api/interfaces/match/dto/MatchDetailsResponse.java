package org.badminton.api.interfaces.match.dto;

import java.util.List;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.domain.match.info.MatchInfo;

public record MatchDetailsResponse(
        Long matchId,
        Long leagueId,
        MatchType matchType,
        SinglesMatchResponse singlesMatch,
        DoublesMatchResponse doublesMatch,
        List<SinglesSetResponse> singlesSets,
        List<DoublesSetResponse> doublesSets
) {

    public static MatchDetailsResponse fromMatchDetailsInfo(MatchInfo.SetScoreDetails matchDetails) {
        return new MatchDetailsResponse(
                matchDetails.getMatchId(),
                matchDetails.getLeagueId(),
                matchDetails.getMatchType(),
                SinglesMatchResponse.fromSinglesMatchInfo(matchDetails.getSinglesMatch()),
                DoublesMatchResponse.fromDoublesMatchInfo(matchDetails.getDoublesMatch()),
                SinglesSetResponse.fromSinglesSetInfoList(matchDetails.getSinglesSets()),
                DoublesSetResponse.fromDoublesSetInfoList(matchDetails.getDoublesSets())
        );
    }
}
