package org.badminton.api.match;

import java.util.ArrayList;
import java.util.List;

import org.badminton.api.common.exception.match.MatchDetailsNotExistException;
import org.badminton.api.common.exception.match.MatchDuplicateException;
import org.badminton.api.common.exception.match.MatchNotExistException;
import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.vo.Team;
import org.badminton.domain.match.repository.DoublesMatchRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DoublesMatchProgress implements MatchProgress {
	private DoublesMatchRepository doublesMatchRepository;

	@Override
	public List<MatchResponse> getAllMatchesInLeague(Long leagueId) {
		return doublesMatchRepository.findAllByLeague_LeagueId(leagueId).stream()
			.map(MatchResponse::entityToDoublesMatchResponse)
			.toList();
	}

	@Override
	public List<MatchDetailsResponse> getAllMatchesDetailsInLeague(Long leagueId) {
		return doublesMatchRepository.findAllByLeague_LeagueId(leagueId).stream()
			.map(MatchDetailsResponse::entityToDoublesMatchDetailsResponse)
			.toList();
	}

	@Override
	public MatchDetailsResponse getMatchDetails(Long matchId) {
		DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId)
			.orElseThrow(() -> new MatchDetailsNotExistException(matchId));
		return MatchDetailsResponse.entityToDoublesMatchDetailsResponse(doublesMatch);
	}

	@Override
	public List<MatchResponse> makeMatches(LeagueEntity league, List<LeagueParticipantEntity> leagueParticipantList) {
		List<DoublesMatchEntity> doublesMatches = makeDoublesMatches(leagueParticipantList, league);
		return doublesMatches
			.stream()
			.map(MatchResponse::entityToDoublesMatchResponse)
			.toList();
	}

	@Override
	public List<MatchDetailsResponse> initDetails(Long leagueId) {
		List<DoublesMatchEntity> doublesMatchList = doublesMatchRepository.findAllByLeague_LeagueId(leagueId);
		return doublesMatchList.stream()
			.map(this::initDoublesMatch)
			.map(MatchDetailsResponse::entityToDoublesMatchDetailsResponse).toList();
	}

	@Override
	public SetScoreUpdateResponse updateSetScore(Long matchId, int setIndex,
		SetScoreUpdateRequest setScoreUpdateRequest) {
		// DoublesSetEntity를 꺼내온다.

		DoublesMatchEntity doublesMatch = doublesMatchRepository.findById(matchId).orElseThrow(
			() -> new MatchNotExistException(matchId, MatchType.DOUBLES));

		// 세트 스코어를 기록한다.
		doublesMatch.getDoublesSets()
			.get(setIndex - 1)
			.saveSetScore(setScoreUpdateRequest.score1(), setScoreUpdateRequest.score2());

		// 승자에 따라 Match에 이긴 세트수를 업데이트해준다. 만약 2번을 모두 이긴 팀이 있다면 해당 Match는 종료된다.
		if (setScoreUpdateRequest.score1() > setScoreUpdateRequest.score2()) {
			doublesMatch.team1WinSet();
		} else {
			doublesMatch.team2WinSet();
		}

		doublesMatchRepository.save(doublesMatch);
		return SetScoreUpdateResponse.doublesSetEntityToSetScoreUpdateResponse(matchId, setIndex,
			doublesMatch.getDoublesSets().get(setIndex - 1));
	}

	@Override
	public void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType) {
		List<DoublesMatchEntity> doublesMatchEntityList = doublesMatchRepository.findAllByLeague_LeagueId(leagueId);
		if (!doublesMatchEntityList.isEmpty())
			throw new MatchDuplicateException(matchType, leagueId);
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