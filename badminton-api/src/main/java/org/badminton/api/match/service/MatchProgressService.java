package org.badminton.api.match.service;

import java.util.List;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.common.exception.match.MatchNotExistException;
import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;
import org.badminton.domain.match.repository.DoublesMatchRepository;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchProgressService {
	private final SinglesMatchRepository singlesMatchRepository;
	private final DoublesMatchRepository doublesMatchRepository;

	private final LeagueRepository leagueRepository;

	public List<MatchDetailsResponse> initMatchDetails(Long clubId, Long leagueId) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		// 경기 타입을 구분하여 처리하기 위해
		MatchType matchType = league.getMatchType();

		if (matchType == MatchType.SINGLES) {
			List<SinglesMatchEntity> singlesMatchList = singlesMatchRepository.findAllByLeague_LeagueId(leagueId);

			return singlesMatchList.stream()
				.map(this::initSinglesMatch)
				.map(MatchDetailsResponse::entityToSinglesMatchDetailsResponse).toList();

		} else if (matchType == MatchType.DOUBLES) {
			List<DoublesMatchEntity> doublesMatchList = doublesMatchRepository.findAllByLeague_LeagueId(leagueId);

			return doublesMatchList.stream()
				.map(this::initDoublesMatch)
				.map(MatchDetailsResponse::entityToDoublesMatchDetailsResponse).toList();
		}
		throw new BadmintonException(ErrorCode.INVALID_PARAMETER, "적절하지 않은 경기 타입입니다.");

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

	// 한 세트가 끝나고 세트 결과를 저장
	public SetScoreUpdateResponse updateSetScore(Long clubId, Long leagueId, Long matchId, int setIndex,
		SetScoreUpdateRequest setScoreUpdateRequest) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		// 경기 타입을 구분하여 처리하기 위해
		MatchType matchType = league.getMatchType();

		// 경기 타입이 단식이면,
		if (matchType == MatchType.SINGLES) {
			// SinglesSetEntity를 꺼내온다.
			SinglesMatchEntity singlesMatch = singlesMatchRepository.findById(matchId).orElseThrow(
				() -> new MatchNotExistException(clubId, leagueId, matchId, matchType)
			);

			// 세트 스코어를 기록한다.
			singlesMatch.getSinglesSets()
				.get(setIndex - 1)
				.saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
			singlesMatchRepository.save(singlesMatch);
			return SetScoreUpdateResponse.singlesSetentityToSetScoreUpdateResponse(
				singlesMatch.getSinglesSets().get(setIndex - 1));
		} else if (matchType == MatchType.DOUBLES) {

			// DoublesSetEntity를 꺼내온다.

			DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId).orElseThrow(
				() -> new MatchNotExistException(clubId, leagueId, matchId, matchType));

			// 세트 스코어를 기록한다.
			doublesMatch.getDoublesSets()
				.get(setIndex - 1)
				.saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
			doublesMatchRepository.save(doublesMatch);
			return SetScoreUpdateResponse.doublesSetEntityToSetScoreUpdateResponse(
				doublesMatch.getDoublesSets().get(setIndex - 1));
		}

		throw new BadmintonException(ErrorCode.INVALID_PARAMETER, "적절하지 않은 경기 타입입니다.");
	}

}
