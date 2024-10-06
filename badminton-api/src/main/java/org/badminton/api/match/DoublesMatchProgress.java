package org.badminton.api.match;

import lombok.AllArgsConstructor;
import org.badminton.api.common.exception.match.MatchNotExistException;
import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.repository.DoublesMatchRepository;

import java.util.List;

@AllArgsConstructor
public class DoublesMatchProgress implements MatchProgress {
    private DoublesMatchRepository doublesMatchRepository;

    @Override
    public List<MatchDetailsResponse> initDetails(Long leagueId) {
        List<DoublesMatchEntity> doublesMatchList = doublesMatchRepository.findAllByLeague_LeagueId(leagueId);

        return doublesMatchList.stream()
                .map(this::initDoublesMatch)
                .map(MatchDetailsResponse::entityToDoublesMatchDetailsResponse).toList();
    }

    @Override
    public SetScoreUpdateResponse updateSetScore(Long matchId, int setIndex, SetScoreUpdateRequest setScoreUpdateRequest) {
        // DoublesSetEntity를 꺼내온다.

        DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId).orElseThrow(
                () -> new MatchNotExistException(matchId, MatchType.DOUBLES));

        // 세트 스코어를 기록한다.
        doublesMatch.getDoublesSets()
                .get(setIndex - 1)
                .saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
        doublesMatchRepository.save(doublesMatch);
        return SetScoreUpdateResponse.doublesSetEntityToSetScoreUpdateResponse(
                doublesMatch.getDoublesSets().get(setIndex - 1));
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
}
