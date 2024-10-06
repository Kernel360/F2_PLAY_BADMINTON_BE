package org.badminton.api.match.service;

import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.match.DoublesMatchProgress;
import org.badminton.api.match.MatchProgress;
import org.badminton.api.match.SinglesMatchProgress;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
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

	// 한 세트가 끝나고 세트 결과를 저장
	public SetScoreUpdateResponse updateSetScore(Long clubId, Long leagueId, Long matchId, int setIndex,
		SetScoreUpdateRequest setScoreUpdateRequest) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		// 경기 타입을 구분하여 처리하기 위해
		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);
		return matchProgress.updateSetScore(matchId, setIndex, setScoreUpdateRequest);
	}

	private MatchProgress createMatchProgress(MatchType matchType) {
		return switch (matchType) {
			case SINGLES -> new SinglesMatchProgress(singlesMatchRepository);
			case DOUBLES -> new DoublesMatchProgress(doublesMatchRepository);
		};
	}
}