package org.badminton.domain.domain.match.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.exception.league.InvalidPlayerCountException;
import org.badminton.domain.common.exception.league.LeagueNotExistException;
import org.badminton.domain.domain.league.entity.League;
import org.badminton.domain.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.domain.league.enums.LeagueStatus;
import org.badminton.domain.domain.match.info.MatchInfo;
import org.badminton.domain.domain.match.info.SetInfo;
import org.badminton.domain.infrastructures.league.LeagueParticipantRepository;
import org.badminton.domain.infrastructures.league.LeagueRepository;
import org.badminton.domain.infrastructures.match.DoublesMatchProgress;
import org.badminton.domain.infrastructures.match.MatchProgress;
import org.badminton.domain.infrastructures.match.SinglesMatchProgress;
import org.badminton.domain.infrastructures.match.repository.DoublesMatchRepository;
import org.badminton.domain.infrastructures.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchInitService {

    private final DoublesMatchRepository doublesMatchRepository;
    private final SinglesMatchRepository singlesMatchRepository;
    private final LeagueParticipantRepository leagueParticipantRepository;
    private final LeagueRepository leagueRepository;

    public List<MatchInfo.Main> getAllMatchesInLeague(Long clubId, Long leagueId) {

        League league = checkIfLeaguePresent(clubId, leagueId);
        MatchType matchType = league.getMatchType();

        MatchProgress matchProgress = createMatchProgress(matchType);

        return matchProgress.getAllMatchesInLeague(leagueId);
    }

    public List<SetInfo.Main> getAllSetsScoreInLeague(Long clubId, Long leagueId) {

        League league = checkIfLeaguePresent(clubId, leagueId);
        MatchType matchType = league.getMatchType();

        MatchProgress matchProgress = createMatchProgress(matchType);

        return matchProgress.getAllMatchesAndSetsScoreInLeague(leagueId);
    }

    public MatchInfo.SetScoreDetails getMatchDetailsInLeague(Long clubId, Long leagueId, Long matchId) {
        League league = checkIfLeaguePresent(clubId, leagueId);
        MatchType matchType = league.getMatchType();

        MatchProgress matchProgress = createMatchProgress(matchType);

        return matchProgress.getMatchDetails(matchId);
    }

    public List<MatchInfo.Main> makeMatches(Long clubId, Long leagueId) {
        // TODO: League의 League Status가 COMPLETED 일 경우에만 생성할 수 있다.
        // TODO: League의 시작 날짜가 되어야 경기를 생성할 수 있다.

        List<LeagueParticipantEntity> leagueParticipantList =
                leagueParticipantRepository.findAllByLeagueLeagueIdAndCanceledFalse(leagueId);

        if (leagueParticipantList.isEmpty()) {
            throw new InvalidPlayerCountException(leagueId, 0);
        }
        League league = leagueParticipantList.get(0).getLeague();
        checkPlayerCount(league, leagueParticipantList.size());
        checkLeagueRecruitingStatus(league);

        MatchType matchType = league.getMatchType();
        MatchProgress matchProgress = createMatchProgress(matchType);

        matchProgress.checkDuplicateMatchInLeague(leagueId, matchType);

        Collections.shuffle(leagueParticipantList);
        return matchProgress.makeMatches(league, leagueParticipantList);
    }

    // TODO: 예외 체이닝 걸 수 있음.
    private void checkLeagueRecruitingStatus(League league) {
        if (league.getLeagueStatus() != LeagueStatus.COMPLETED) {
            league.cancelLeague();
            throw new InvalidPlayerCountException(league.getLeagueId(), league.getRecruitingClosedAt());
        }
    }

    private void checkPlayerCount(League league, int playerCount) {
        if (league.getPlayerLimitCount() != playerCount) {
            throw new InvalidPlayerCountException(league.getLeagueId(), playerCount);
        }
        league.completeLeagueRecruiting();
    }

    private MatchProgress createMatchProgress(MatchType matchType) {
        return switch (matchType) {
            case SINGLES -> new SinglesMatchProgress(singlesMatchRepository);
            case DOUBLES -> new DoublesMatchProgress(doublesMatchRepository);
        };
    }

    private League checkIfLeaguePresent(Long clubId, Long leagueId) {
        return leagueRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));
    }
}
