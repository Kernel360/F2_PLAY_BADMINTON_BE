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
import org.badminton.domain.league.enums.LeagueStatus;
import org.badminton.domain.league.repository.LeagueParticipantRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.badminton.domain.match.repository.DoublesMatchRepository;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchInitService {

	private final DoublesMatchRepository doublesMatchRepository;
	private final SinglesMatchRepository singlesMatchRepository;
	private final LeagueParticipantRepository leagueParticipantRepository;
	private final LeagueRepository leagueRepository;

	public List<MatchResponse> getAllMatchesInLeague(Long clubId, Long leagueId) {

		LeagueEntity league = checkIfLeaguePresent(clubId, leagueId);
		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);

		return matchProgress.getAllMatchesInLeague(leagueId);
	}

	public List<MatchDetailsResponse> getAllMatchesDetailsInLeague(Long clubId, Long leagueId) {

		LeagueEntity league = checkIfLeaguePresent(clubId, leagueId);
		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);

		return matchProgress.getAllMatchesDetailsInLeague(leagueId);
	}

	public MatchDetailsResponse getMatchDetailsInLeague(Long clubId, Long leagueId, Long matchId) {
		LeagueEntity league = checkIfLeaguePresent(clubId, leagueId);
		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);

		return matchProgress.getMatchDetails(matchId);
	}

	public List<MatchResponse> makeMatches(Long leagueId) {
		// TODO: League의 League Status가 COMPLETED 일 경우에만 생성할 수 있다.
		// TODO: League의 시작 날짜가 되어야 경기를 생성할 수 있다.

		List<LeagueParticipantEntity> leagueParticipantList =
			leagueParticipantRepository.findAllByLeagueLeagueIdAndCanceledFalse(leagueId);

		if (leagueParticipantList.isEmpty()) {
			throw new InvalidPlayerCountException(leagueId, 0);
		}
		LeagueEntity league = leagueParticipantList.get(0).getLeague();
		checkPlayerCount(league, leagueParticipantList.size());
		checkLeagueRecruitingStatus(league);

		MatchType matchType = league.getMatchType();
		MatchProgress matchProgress = createMatchProgress(matchType);

		matchProgress.checkDuplicateMatchInLeague(leagueId, matchType);

		Collections.shuffle(leagueParticipantList);

		return matchProgress.makeMatches(league, leagueParticipantList);
	}

	public List<MatchDetailsResponse> initMatchDetails(Long clubId, Long leagueId) {
		// 경기 일정이 있는지 확인하고 꺼내기
		LeagueEntity league = checkIfLeaguePresent(clubId, leagueId);
		MatchType matchType = league.getMatchType();

		MatchProgress matchProgress = createMatchProgress(matchType);
		return matchProgress.initDetails(leagueId);
	}

	// TODO: 예외 체이닝 걸 수 있음.
	private void checkLeagueRecruitingStatus(LeagueEntity league) {
		if (league.getLeagueStatus() != LeagueStatus.COMPLETED) {
			league.cancelLeague();
			throw new InvalidPlayerCountException(league.getLeagueId(), league.getRecruitingClosedAt());
		}
	}

	private void checkPlayerCount(LeagueEntity league, int playerCount) {
		if (league.getPlayerLimitCount() != playerCount) {
			throw new InvalidPlayerCountException(league.getLeagueId(), playerCount);
		}
		league.completeLeagueRecruiting();
	}

	private MatchProgress createMatchProgress(MatchType matchType) {
		return switch (matchType) {
			case SINGLES -> new SinglesMatchProgress(singlesMatchRepository);
			case DOUBLES -> new DoublesMatchProgress(doublesMatchRepository);
		};
	}

	private LeagueEntity checkIfLeaguePresent(Long clubId, Long leagueId) {
		return leagueRepository.findById(leagueId)
			.orElseThrow(() -> new LeagueNotExistException(clubId, leagueId));
	}
}
