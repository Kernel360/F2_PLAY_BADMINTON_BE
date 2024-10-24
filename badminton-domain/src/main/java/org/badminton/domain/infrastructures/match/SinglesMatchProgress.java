package org.badminton.domain.infrastructures.match;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.exception.match.MatchDetailsNotExistException;
import org.badminton.domain.common.exception.match.MatchDuplicateException;
import org.badminton.domain.common.exception.match.MatchNotExistException;
import org.badminton.domain.domain.league.entity.League;
import org.badminton.domain.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.domain.match.command.MatchCommand;
import org.badminton.domain.domain.match.entity.SinglesMatchEntity;
import org.badminton.domain.domain.match.entity.SinglesSetEntity;
import org.badminton.domain.domain.match.info.MatchInfo;
import org.badminton.domain.domain.match.info.SetInfo;
import org.badminton.domain.infrastructures.match.repository.SinglesMatchRepository;

@Slf4j
@AllArgsConstructor
public class SinglesMatchProgress implements MatchProgress {

    private SinglesMatchRepository singlesMatchRepository;

    @Override
    public List<MatchInfo.Main> getAllMatchesInLeague(Long leagueId) {
        return singlesMatchRepository.findAllByLeague_LeagueId(leagueId)
                .stream()
                .map(MatchInfo.Main::fromSinglesMatchToMain)
                .toList();
    }

    @Override
    public List<SetInfo.Main> getAllMatchesAndSetsScoreInLeague(Long leagueId) {
        return singlesMatchRepository.findAllByLeague_LeagueId(leagueId)
                .stream()
                .flatMap(singlesMatch ->
                        singlesMatch.getSinglesSets().stream()
                                .map(singlesSet -> SetInfo.fromSinglesSet(
                                        singlesMatch.getSinglesMatchId(),
                                        singlesSet.getSetIndex(), singlesSet))
                )
                .toList();
    }

    @Override
    public MatchInfo.SetScoreDetails getMatchDetails(Long matchId) {
        SinglesMatchEntity singlesMatch = singlesMatchRepository.findById(matchId)
                .orElseThrow(() -> new MatchDetailsNotExistException(matchId));
        return MatchInfo.SetScoreDetails.fromSinglesMatchToMatchDetails(singlesMatch);
    }

    @Override
    public List<MatchInfo.Main> makeMatches(League league, List<LeagueParticipantEntity> leagueParticipantList) {
        List<SinglesMatchEntity> singlesMatches = makeSinglesMatches(leagueParticipantList, league);
        return singlesMatches.stream()
                .map(this::initSinglesMatch)
                .map(MatchInfo.Main::fromSinglesMatchToMain)
                .toList();
    }

    @Override
    public SetInfo.Main updateSetScore(Long matchId, int setIndex,
                                       MatchCommand.UpdateSetScore updateSetScoreCommand) {
        // SinglesSetEntity를 꺼내온다.
        SinglesMatchEntity singlesMatch = singlesMatchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotExistException(matchId, MatchType.SINGLES));

        // 세트 스코어를 기록한다.
        singlesMatch.getSinglesSets()
                .get(setIndex - 1)
                .saveSetScore(updateSetScoreCommand.getScore1(), updateSetScoreCommand.getScore2());

        // 승자에 따라 Match에 이긴 세트수를 업데이트해준다. 만약 2번을 모두 이긴 참가자가 있다면 해당 Match는 종료된다.
        if (updateSetScoreCommand.getScore1() > updateSetScoreCommand.getScore2()) {
            singlesMatch.player1WinSet();
        } else {
            singlesMatch.player2WinSet();
        }

        singlesMatchRepository.save(singlesMatch);
        return SetInfo.fromSinglesSet(matchId, setIndex, singlesMatch.getSinglesSets().get(setIndex - 1));
    }

    @Override
    public void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType) {
        List<SinglesMatchEntity> singlesMatchEntityList = singlesMatchRepository.findAllByLeague_LeagueId(leagueId);
        if (!singlesMatchEntityList.isEmpty()) {
            throw new MatchDuplicateException(matchType, leagueId);
        }
    }

    // TODO: 리팩토링
    private SinglesMatchEntity initSinglesMatch(SinglesMatchEntity singlesMatch) {
        //단식 게임 세트를 3개 생성
        SinglesSetEntity set1 = new SinglesSetEntity(singlesMatch, 1);
        SinglesSetEntity set2 = new SinglesSetEntity(singlesMatch, 2);
        SinglesSetEntity set3 = new SinglesSetEntity(singlesMatch, 3);

        singlesMatch.addSet(set1);
        singlesMatch.addSet(set2);
        singlesMatch.addSet(set3);

        singlesMatchRepository.save(singlesMatch);
        return singlesMatch;
    }

    private List<SinglesMatchEntity> makeSinglesMatches(List<LeagueParticipantEntity> leagueParticipantList,
                                                        League league) {

        List<SinglesMatchEntity> singlesMatches = new ArrayList<>();
        for (int i = 0; i < leagueParticipantList.size() - 1; i += 2) {
            SinglesMatchEntity singlesMatch = new SinglesMatchEntity(league, leagueParticipantList.get(i),
                    leagueParticipantList.get(i + 1));
            singlesMatches.add(singlesMatch);
            singlesMatchRepository.save(singlesMatch);
        }
        return singlesMatches;
    }
}

