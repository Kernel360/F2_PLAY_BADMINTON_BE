package org.badminton.api.match.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.api.common.exception.league.InvalidPlayerCountException;
import org.badminton.api.common.exception.match.MatchDuplicateException;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.league.repository.LeagueParticipantRepository;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.badminton.domain.match.model.vo.Team;
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

	public List<MatchResponse> makeMatches(Long leagueId) {
		List<LeagueParticipantEntity> leagueParticipantList =
			leagueParticipantRepository.findAllByLeague_LeagueId(leagueId);

		LeagueEntity league = leagueParticipantList.get(0).getLeague();
		MatchType matchType = league.getMatchType();

		checkPlayerCount(league, leagueParticipantList.size());
		checkDuplicateMatchInLeague(leagueId, matchType);

		Collections.shuffle(leagueParticipantList);
		if (matchType == MatchType.SINGLE) {
			List<SinglesMatchEntity> singlesMatches = makeSinglesMatches(leagueParticipantList, league);
			return singlesMatches
				.stream()
				.map(MatchResponse::entityToSinglesMatchResponse)
				.toList();
		} else if (matchType == MatchType.DOUBLES) {
			List<DoublesMatchEntity> doublesMatches = makeDoublesMatches(leagueParticipantList, league);
			return doublesMatches
				.stream()
				.map(MatchResponse::entityToDoublesMatchResponse)
				.toList();
		} else
			throw new BadmintonException(ErrorCode.BAD_REQUEST, "존재하지 않는 경기 타입입니다.");
	}

	private void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType) {
		if (matchType == MatchType.SINGLE) {
			singlesMatchRepository.findByLeague_LeagueId(leagueId).ifPresent(
				singlesMatch -> {
					throw new MatchDuplicateException(matchType, singlesMatch.getSinglesMatchId());
				}
			);
		} else if (matchType == MatchType.DOUBLES) {
			doublesMatchRepository.findByLeague_LeagueId(leagueId).ifPresent(
				doublesMatch -> {
					throw new MatchDuplicateException(matchType, doublesMatch.getDoublesMatchId());
				}
			);
		}
	}

	private void checkPlayerCount(LeagueEntity league, int playerCount) {
		if (league.getPlayerCount() != playerCount) {
			throw new InvalidPlayerCountException(league.getLeagueId(), playerCount);
		}
	}

	private List<SinglesMatchEntity> makeSinglesMatches(List<LeagueParticipantEntity> leagueParticipantList,
		LeagueEntity league) {

		List<SinglesMatchEntity> singlesMatches = new ArrayList<>();
		for (int i = 0; i < leagueParticipantList.size() - 1; i += 2) {
			SinglesMatchEntity singlesMatch = new SinglesMatchEntity(
				league, leagueParticipantList.get(i), leagueParticipantList.get(i + 1)
			);
			singlesMatches.add(singlesMatch);
			singlesMatchRepository.save(singlesMatch);
		}
		return singlesMatches;
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
