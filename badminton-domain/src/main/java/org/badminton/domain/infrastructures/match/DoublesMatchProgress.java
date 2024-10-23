package org.badminton.domain.infrastructures.match;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.exception.match.MatchDetailsNotExistException;
import org.badminton.domain.common.exception.match.MatchDuplicateException;
import org.badminton.domain.common.exception.match.MatchNotExistException;
import org.badminton.domain.domain.league.entity.LeagueEntity;
import org.badminton.domain.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.domain.league.vo.Team;
import org.badminton.domain.domain.match.command.MatchCommand;
import org.badminton.domain.domain.match.entity.DoublesMatchEntity;
import org.badminton.domain.domain.match.entity.DoublesSetEntity;
import org.badminton.domain.domain.match.info.MatchInfo;
import org.badminton.domain.domain.match.info.SetInfo;
import org.badminton.domain.infrastructures.match.repository.DoublesMatchRepository;

@AllArgsConstructor
public class DoublesMatchProgress implements MatchProgress {
    private DoublesMatchRepository doublesMatchRepository;

    @Override
    public List<MatchInfo.Main> getAllMatchesInLeague(Long leagueId) {
        return doublesMatchRepository.findAllByLeague_LeagueId(leagueId).stream()
                .map(MatchInfo.Main::fromDoublesMatchToMain)
                .toList();
    }

    @Override
    public List<SetInfo.Main> getAllMatchesAndSetsScoreInLeague(Long leagueId) {
        return doublesMatchRepository.findAllByLeague_LeagueId(leagueId).stream()
                .flatMap(doublesMatch ->
                        doublesMatch.getDoublesSets().stream()
                                .map(doublesSet -> SetInfo.fromDoublesSet(doublesMatch.getDoublesMatchId(),
                                        doublesSet.getSetIndex(), doublesSet))
                )
                .toList();
    }

    @Override
    public MatchInfo.SetScoreDetails getMatchDetails(Long matchId) {
        DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId)
                .orElseThrow(() -> new MatchDetailsNotExistException(matchId));
        return MatchInfo.SetScoreDetails.fromDoublesMatchToMatchDetails(doublesMatch);
    }

    @Override
    public List<MatchInfo.Main> makeMatches(LeagueEntity league, List<LeagueParticipantEntity> leagueParticipantList) {
        List<DoublesMatchEntity> doublesMatches = makeDoublesMatches(leagueParticipantList, league);
        return doublesMatches
                .stream()
                .map(this::initDoublesMatch)
                .map(MatchInfo.Main::fromDoublesMatchToMain)
                .toList();
    }

    @Override
    public SetInfo.Main updateSetScore(Long matchId, int setIndex,
                                       MatchCommand.UpdateSetScore updateSetScoreCommand) {
        // DoublesSetEntity를 꺼내온다.

        DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId).orElseThrow(
                () -> new MatchNotExistException(matchId, MatchType.DOUBLES));

        // 세트 스코어를 기록한다.
        doublesMatch.getDoublesSets()
                .get(setIndex - 1)
                .saveSetScore(updateSetScoreCommand.getScore1(), updateSetScoreCommand.getScore2());

        // 승자에 따라 Match에 이긴 세트수를 업데이트해준다. 만약 2번을 모두 이긴 팀이 있다면 해당 Match는 종료된다.
        if (updateSetScoreCommand.getScore1() > updateSetScoreCommand.getScore2()) {
            doublesMatch.team1WinSet();
        } else {
            doublesMatch.team2WinSet();
        }

        doublesMatchRepository.save(doublesMatch);
        return SetInfo.fromDoublesSet(matchId, setIndex, doublesMatch.getDoublesSets().get(setIndex - 1));
    }

    @Override
    public void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType) {
        List<DoublesMatchEntity> doublesMatchEntityList = doublesMatchRepository.findAllByLeague_LeagueId(leagueId);
        if (!doublesMatchEntityList.isEmpty()) {
            throw new MatchDuplicateException(matchType, leagueId);
        }
    }

    private DoublesMatchEntity initDoublesMatch(DoublesMatchEntity doublesMatch) {
        // 복식 게임 세트를 3개 생성
        DoublesSetEntity set1 = new DoublesSetEntity(doublesMatch, 1);
        DoublesSetEntity set2 = new DoublesSetEntity(doublesMatch, 2);
        DoublesSetEntity set3 = new DoublesSetEntity(doublesMatch, 3);

        // 복식 게임 결과를 생성
        doublesMatch.addSet(set1);
        doublesMatch.addSet(set2);
        doublesMatch.addSet(set3);

        doublesMatchRepository.save(doublesMatch);
        return doublesMatch;
    }

    private List<DoublesMatchEntity> makeDoublesMatches(List<LeagueParticipantEntity> leagueParticipantList,
                                                        LeagueEntity league) {

        List<DoublesMatchEntity> doublesMatches = new ArrayList<>();
        for (int i = 0; i < leagueParticipantList.size() - 3; i += 4) {
            Team team1 = new Team(leagueParticipantList.get(i), leagueParticipantList.get(i + 1));
            Team team2 = new Team(leagueParticipantList.get(i + 2), leagueParticipantList.get(i + 3));
            DoublesMatchEntity doublesMatch = new DoublesMatchEntity(league, team1, team2);
            doublesMatches.add(doublesMatch);
            doublesMatchRepository.save(doublesMatch);
        }
        return doublesMatches;
    }
}