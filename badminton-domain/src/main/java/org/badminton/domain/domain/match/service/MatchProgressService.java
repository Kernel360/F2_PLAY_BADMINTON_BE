package org.badminton.domain.domain.match.service;

import lombok.RequiredArgsConstructor;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.exception.league.LeagueNotExistException;
import org.badminton.domain.domain.league.entity.LeagueEntity;
import org.badminton.domain.domain.match.command.MatchCommand;
import org.badminton.domain.domain.match.info.SetInfo;
import org.badminton.domain.infrastructures.league.LeagueRepository;
import org.badminton.domain.infrastructures.match.DoublesMatchProgress;
import org.badminton.domain.infrastructures.match.MatchProgress;
import org.badminton.domain.infrastructures.match.SinglesMatchProgress;
import org.badminton.domain.infrastructures.match.repository.DoublesMatchRepository;
import org.badminton.domain.infrastructures.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchProgressService {
    private final SinglesMatchRepository singlesMatchRepository;
    private final DoublesMatchRepository doublesMatchRepository;

    private final LeagueRepository leagueRepository;

    // 한 세트가 끝나고 세트 결과를 저장
    public SetInfo.Main updateSetScore(Long clubId, Long leagueId, Long matchId, int setIndex,
                                       MatchCommand.UpdateSetScore updateSetScoreCommand) {
        // 경기 일정이 있는지 확인하고 꺼내기
        LeagueEntity league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

        // 경기 타입을 구분하여 처리하기 위해
        MatchType matchType = league.getMatchType();

        MatchProgress matchProgress = createMatchProgress(matchType);
        return matchProgress.updateSetScore(matchId, setIndex, updateSetScoreCommand);
    }

    private MatchProgress createMatchProgress(MatchType matchType) {
        return switch (matchType) {
            case SINGLES -> new SinglesMatchProgress(singlesMatchRepository);
            case DOUBLES -> new DoublesMatchProgress(doublesMatchRepository);
        };
    }
}