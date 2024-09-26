package org.badminton.api.league.service;

import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueStatusUpdateResponse;
import org.badminton.api.league.model.dto.LeagueUpdateRequest;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepository;
	private final ClubRepository clubRepository;

	public LeagueCreateResponse createLeague(Long clubId, LeagueCreateRequest leagueCreateRequest) {

		ClubEntity club = provideClub(clubId);

		// TODO: 개선 방법
		LeagueEntity league = new LeagueEntity(leagueCreateRequest.leagueName(),
			leagueCreateRequest.description(), leagueCreateRequest.leagueAt(), leagueCreateRequest.tierLimit(),
			leagueCreateRequest.closedAt(), leagueCreateRequest.leagueStatus(), leagueCreateRequest.playerCount(),
			leagueCreateRequest.matchType(), leagueCreateRequest.matchingRequirement(), club);

		return LeagueCreateResponse.leagueCreateEntityToResponse(leagueRepository.save(league));
	}

	public LeagueReadResponse getLeague(Long clubId, Long leagueId) {
		ClubEntity club = provideClub(clubId);
		LeagueEntity league = provideLeagueIfLeagueInClub(club.getClubId(), leagueId);
		return LeagueReadResponse.leagueReadEntityToResponse(league);
	}

	public LeagueStatusUpdateResponse updateLeague(Long clubId, Long leagueId, LeagueUpdateRequest leagueUpdateRequest
	) {
		LeagueEntity league = provideLeagueIfLeagueInClub(clubId, leagueId);

		// TODO: 개선 방법
		league.updateLeague(leagueUpdateRequest.leagueName(),
			leagueUpdateRequest.description(), leagueUpdateRequest.tierLimit(),
			leagueUpdateRequest.leagueAt(), leagueUpdateRequest.closedAt(), leagueUpdateRequest.leagueStatus(),
			leagueUpdateRequest.playerCount(),
			leagueUpdateRequest.matchType(), leagueUpdateRequest.matchingRequirement());

		leagueRepository.save(league);
		return new LeagueStatusUpdateResponse(league);
	}

	public void deleteLeague(Long clubId, Long leagueId) {
		LeagueEntity league = provideLeagueIfLeagueInClub(clubId, leagueId);
		leagueRepository.deleteByLeagueId(league.getLeagueId());
	}

	private LeagueEntity provideLeagueIfLeagueInClub(Long clubId, Long leagueId) {
		return leagueRepository.findByClubClubIdAndLeagueId(clubId, leagueId).orElseThrow(
			() -> new LeagueNotExistException(clubId, leagueId));
	}

	private ClubEntity provideClub(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId).orElseThrow(
			() -> new ClubNotExistException(clubId));
	}
}
