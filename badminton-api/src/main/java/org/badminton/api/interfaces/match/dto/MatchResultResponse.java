package org.badminton.api.interfaces.match.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchStatus;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.domain.match.entity.DoublesMatchEntity;
import org.badminton.domain.domain.match.entity.SinglesMatchEntity;

public record MatchResultResponse(
        Long matchId,
        Long leagueId,
        MatchType matchType,
        SinglesMatchResultResponse singlesMatch,
        DoublesMatchResultResponse doublesMatch,
        MatchStatus matchStatus,
        LocalDateTime leagueAt
) {
    public static MatchResultResponse fromSinglesMatchEntity(SinglesMatchEntity singlesMatch, Long clubMemberId) {
        boolean isPlayer1 = singlesMatch.getLeagueParticipant1().getClubMember().getClubMemberId().equals(clubMemberId);
        return new MatchResultResponse(
                singlesMatch.getSinglesMatchId(),
                singlesMatch.getLeague().getLeagueId(),
                MatchType.SINGLES,
                SinglesMatchResultResponse.fromSinglesMatch(singlesMatch, isPlayer1, clubMemberId),
                null,
                singlesMatch.getMatchStatus()
                , singlesMatch.getLeague().getLeagueAt()
        );
    }

    public static MatchResultResponse fromDoublesMatchEntity(DoublesMatchEntity doublesMatch, Long clubMemberId) {
        boolean isTeam1 = isPlayerInTeam1(doublesMatch, clubMemberId);
        return new MatchResultResponse(
                doublesMatch.getDoublesMatchId(),
                doublesMatch.getLeague().getLeagueId(),
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
