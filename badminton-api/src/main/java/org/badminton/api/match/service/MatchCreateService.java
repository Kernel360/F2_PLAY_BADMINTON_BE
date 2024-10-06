package org.badminton.api.match.service;

import java.util.Collections;
import java.util.List;

import org.badminton.api.common.exception.league.InvalidPlayerCountException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.match.DoublesMatchProgress;
import org.badminton.api.match.MatchProgress;
import org.badminton.api.match.SinglesMatchProgress;
import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.league.repository.LeagueParticipantRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.badminton.domain.match.repository.DoublesMatchRepository;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchCreateService {

	private final DoublesMatchRepository doublesMatchRepository;
	private final SinglesMatchRepository singlesMatchRepository;
	private final LeagueParticipantRepository leagueParticipantRepository;
	private final LeagueRepository leagueRepository;

	public List<MatchResponse> makeMatches(Long leagueId) {
		// TODO: 만약 리스트에 아무것도 없으면!?
		List<LeagueParticipantEntity> leagueParticipantList =
			leagueParticipantRepository.findAllByLeague_LeagueId(leagueId);

		LeagueEntity league = leagueParticipantList.get(0).getLeague();
		checkPlayerCount(league, leagueParticipantList.size());

		MatchType matchType = league.getMatchType();
		MatchProgress matchProgress = createMatchProgress(matchType);

		matchProgress.checkDuplicateMatchInLeague(leagueId, matchType);

		Collections.shuffle(leagueParticipantList);

		return matchProgress.makeMatches(league, leagueParticipantList);
	}

	public List<MatchDetailsResponse> initMatchDetails(Long clubId, Long leagueId) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));

		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);
		return matchProgress.initDetails(leagueId);
	}

	private void checkPlayerCount(LeagueEntity league, int playerCount) {
		if (league.getPlayerCount() != playerCount) {
			throw new InvalidPlayerCountException(league.getLeagueId(), playerCount);
		}
	}

	private MatchProgress createMatchProgress(MatchType matchType) {
		return switch (matchType) {
			case SINGLES -> new SinglesMatchProgress(singlesMatchRepository);
			case DOUBLES -> new DoublesMatchProgress(doublesMatchRepository);
		};
	}
}
