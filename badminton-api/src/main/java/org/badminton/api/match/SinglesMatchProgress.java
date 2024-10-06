package org.badminton.api.match;

import lombok.AllArgsConstructor;
import org.badminton.api.common.exception.match.MatchNotExistException;
import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;
import org.badminton.domain.match.repository.SinglesMatchRepository;

import java.util.List;

@AllArgsConstructor
public class SinglesMatchProgress implements MatchProgress {
    private SinglesMatchRepository singlesMatchRepository;

    @Override
    public List<MatchDetailsResponse> initDetails(Long leagueId) {
        List<SinglesMatchEntity> singlesMatchList = singlesMatchRepository.findAllByLeague_LeagueId(leagueId);

        return singlesMatchList.stream()
                .map(this::initSinglesMatch)
                .map(MatchDetailsResponse::entityToSinglesMatchDetailsResponse).toList();
    }

    @Override
    public SetScoreUpdateResponse updateSetScore(Long matchId, int setIndex, SetScoreUpdateRequest setScoreUpdateRequest) {
        // SinglesSetEntity를 꺼내온다.
        SinglesMatchEntity singlesMatch = singlesMatchRepository.findById(matchId).orElseThrow(
                () -> new MatchNotExistException(matchId, MatchType.SINGLES)
        );

        // 세트 스코어를 기록한다.
        singlesMatch.getSinglesSets()
                .get(setIndex - 1)
                .saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
        singlesMatchRepository.save(singlesMatch);
        return SetScoreUpdateResponse.singlesSetentityToSetScoreUpdateResponse(
                singlesMatch.getSinglesSets().get(setIndex - 1));
    }

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
}
