package org.badminton.api.match.service;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.common.exception.match.MatchNotExistException;
import org.badminton.api.common.exception.match.SetNotExistException;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.DoublesMatchResultEntity;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchResultEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;
import org.badminton.domain.match.repository.DoublesMatchRepository;
import org.badminton.domain.match.repository.DoublesMatchResultRepository;
import org.badminton.domain.match.repository.DoublesSetRepository;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.badminton.domain.match.repository.SinglesMatchResultRepository;
import org.badminton.domain.match.repository.SinglesSetRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchProgressService {
	private final SinglesSetRepository singlesSetRepository;
	private final DoublesSetRepository doublesSetRepository;
	private final SinglesMatchResultRepository singlesMatchResultRepository;
	private final DoublesMatchResultRepository doublesMatchResultRepository;

	private final SinglesMatchRepository singlesMatchRepository;
	private final DoublesMatchRepository doublesMatchRepository;

	private final LeagueRepository leagueRepository;

	public void initMatch(Long clubId, Long leagueId, Long matchId) {

		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		// 경기 타입을 구분하여 처리하기 위해
		MatchType matchType = league.getMatchType();

		// 단식 게임이면,
		if (matchType == MatchType.SINGLE) {
			// SinglesMatchEntity를 꺼내오기
			SinglesMatchEntity singlesMatch = singlesMatchRepository.findById(matchId)
				.orElseThrow(() -> new MatchNotExistException(clubId, leagueId, matchId, matchType));

			//단식 게임 세트를 3개 생성
			SinglesSetEntity set1 = new SinglesSetEntity(singlesMatch, 1);
			SinglesSetEntity set2 = new SinglesSetEntity(singlesMatch, 2);
			SinglesSetEntity set3 = new SinglesSetEntity(singlesMatch, 3);

			singlesSetRepository.save(set1);
			singlesSetRepository.save(set2);
			singlesSetRepository.save(set3);

			// 단식 게임 결과를 생성
			SinglesMatchResultEntity matchResult = new SinglesMatchResultEntity(singlesMatch);

			singlesMatchResultRepository.save(matchResult);

		}
		/// 복식 게임이면,
		else if (matchType == MatchType.DOUBLES) {
			// DoublesMatchEntity를 꺼내오기
			DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId)
				.orElseThrow(() -> new MatchNotExistException(clubId, leagueId, matchId, matchType));

			// 복식 게임 세트를 3개 생성
			DoublesSetEntity set1 = new DoublesSetEntity(doublesMatch, 1);
			DoublesSetEntity set2 = new DoublesSetEntity(doublesMatch, 2);
			DoublesSetEntity set3 = new DoublesSetEntity(doublesMatch, 3);

			doublesSetRepository.save(set1);
			doublesSetRepository.save(set2);
			doublesSetRepository.save(set3);

			// 복식 게임 결과를 생성
			DoublesMatchResultEntity matchResult = new DoublesMatchResultEntity(doublesMatch);

			doublesMatchResultRepository.save(matchResult);
		}
	}

	public SetScoreUpdateResponse updateSetScore(Long clubId, Long leagueId, Long matchId, Long setId,
		SetScoreUpdateRequest setScoreUpdateRequest) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		// 경기 타입을 구분하여 처리하기 위해
		MatchType matchType = league.getMatchType();

		// 경기 타입이 단식이면,
		if (matchType == MatchType.SINGLE) {
			// SinglesSetEntity를 꺼내온다.
			SinglesSetEntity singlesSet = singlesSetRepository.findById(setId).orElseThrow(
				() -> new SetNotExistException(leagueId, matchId, setId, matchType));

			// 세트 스코어를 기록한다.
			singlesSet.saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
			return SetScoreUpdateResponse.singlesSetentityToSetScoreUpdateResponse(singlesSet);
		} else if (matchType == MatchType.DOUBLES) {

			// DoublesSetEntity를 꺼내온다.

			DoublesSetEntity doublesSet = doublesSetRepository.findById(setId).orElseThrow(
				() -> new SetNotExistException(leagueId, matchId, setId, matchType));

			// 세트 스코어를 기록한다.
			doublesSet.saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());
			return SetScoreUpdateResponse.doublesSetEntityToSetScoreUpdateResponse(doublesSet);
		}

		throw new BadmintonException(ErrorCode.INVALID_PARAMETER, "적절하지 않은 경기 타입입니다.");
	}

}
