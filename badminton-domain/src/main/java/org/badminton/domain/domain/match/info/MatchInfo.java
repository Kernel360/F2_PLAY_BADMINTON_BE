package org.badminton.domain.domain.match.info;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.badminton.domain.common.enums.MatchStatus;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.domain.match.entity.DoublesMatchEntity;
import org.badminton.domain.domain.match.entity.SinglesMatchEntity;

public class MatchInfo {

    @Getter
    @Builder
    @ToString
    public static class Main {
        private final Long matchId;
        private final Long leagueId;
        private final MatchType matchType;
        private final SinglesMatchInfo singlesMatchInfo;
        private final DoublesMatchInfo doublesMatchInfo;
        private final MatchStatus matchStatus;
    }

    public static Main fromSinglesMatchToMain(SinglesMatchEntity singlesMatch) {
        return Main.builder()
                .leagueId(singlesMatch.getSinglesMatchId())
                .matchType(MatchType.SINGLES)
                .singlesMatchInfo(SinglesMatchInfo.fromSinglesMatch(singlesMatch))
                .matchStatus(singlesMatch.getMatchStatus())
                .build();
    }

    public static Main fromDoublesMatchToMain(DoublesMatchEntity doublesMatch) {
        return Main.builder()
                .leagueId(doublesMatch.getDoublesMatchId())
                .matchType(MatchType.DOUBLES)
                .doublesMatchInfo(DoublesMatchInfo.fromDoublesMatchEntity(doublesMatch))
                .matchStatus(doublesMatch.getMatchStatus())
                .build();
    }

    @Getter
    @Builder
    @ToString
    public static class SetScoreDetails {
        Long matchId;
        Long leagueId;
        MatchType matchType;
        SinglesMatchInfo singlesMatch;
        DoublesMatchInfo doublesMatch;
        List<SinglesSetInfo> singlesSets;
        List<DoublesSetInfo> doublesSets;
    }

    public static SetScoreDetails fromSinglesMatchToMatchDetails(SinglesMatchEntity singlesMatch) {

        return SetScoreDetails.builder()
                .matchId(singlesMatch.getSinglesMatchId())
                .leagueId(singlesMatch.getLeague().getLeagueId())
                .matchType(MatchType.SINGLES)
                .singlesMatch(SinglesMatchInfo.fromSinglesMatch(singlesMatch))
                .singlesSets(SinglesSetInfo.fromSinglesSets(singlesMatch.getSinglesSets()))
                .build();

    }

    public static SetScoreDetails fromDoublesMatchToMatchDetails(DoublesMatchEntity doublesMatch) {

        return SetScoreDetails.builder()
                .matchId(doublesMatch.getDoublesMatchId())
                .leagueId(doublesMatch.getLeague().getLeagueId())
                .matchType(MatchType.DOUBLES)
                .doublesMatch(DoublesMatchInfo.fromDoublesMatchEntity(doublesMatch))
                .doublesSets(DoublesSetInfo.fromDoublesSets(doublesMatch.getDoublesSets()))
                .build();

    }

}
