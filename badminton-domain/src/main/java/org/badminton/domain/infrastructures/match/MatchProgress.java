package org.badminton.domain.infrastructures.match;

import java.util.List;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.domain.league.entity.League;
import org.badminton.domain.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.domain.match.command.MatchCommand;
import org.badminton.domain.domain.match.info.MatchInfo;
import org.badminton.domain.domain.match.info.MatchInfo.SetScoreDetails;
import org.badminton.domain.domain.match.info.SetInfo;

public interface MatchProgress {

    List<MatchInfo.Main> getAllMatchesInLeague(Long leagueId);

    List<SetInfo.Main> getAllMatchesAndSetsScoreInLeague(Long leagueId);

    SetScoreDetails getMatchDetails(Long matchId);

    List<MatchInfo.Main> makeMatches(League league, List<LeagueParticipantEntity> leagueParticipantList);

    SetInfo.Main updateSetScore(Long matchId, int setIndex, MatchCommand.UpdateSetScore updateSetScoreCommand);

    void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType);
}
