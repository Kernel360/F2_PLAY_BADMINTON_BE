package org.badminton.api.league.service;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueStatusUpdateRequest;
import org.badminton.api.league.model.dto.LeagueStatusUpdateResponse;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepository;

	public LeagueCreateResponse createLeague(LeagueCreateRequest createRequest) {
		LeagueEntity savedLeague = leagueRepository.save(
			createRequest.leagueCreateRequestToEntity());
		return LeagueCreateResponse.leagueCreateEntityToResponse(savedLeague);
	}

	public LeagueReadResponse getLeague(Long leagueId) {
		LeagueEntity findResult = leagueRepository.findById(leagueId).orElseThrow(() ->
			new LeagueNotExistException(ErrorCode.RESOURCE_ALREADY_EXIST, String.valueOf(leagueId)));
		return LeagueReadResponse.leagueReadEntityToResponse(findResult);
	}

	public LeagueStatusUpdateResponse updateLeagueStatus(LeagueStatusUpdateRequest updateRequest) {
		LeagueEntity findLeague = leagueRepository.findById(updateRequest.leagueId()).orElseThrow(() ->
			new LeagueNotExistException(ErrorCode.CLUB_NOT_EXIST, String.valueOf(updateRequest.leagueId())));
		LeagueEntity updatedLeague = LeagueEntity.updateLeague(findLeague.getLeagueId(), findLeague.getLeagueName(),
			findLeague.getDescription(), findLeague.getLeagueAt(), findLeague.getTierLimit(), findLeague.getClosedAt(),
			updateRequest.status(), findLeague.getPlayerCount(), findLeague.getMatchType(),
			findLeague.getMatchingRequirement());
		return new LeagueStatusUpdateResponse(leagueRepository.save(updatedLeague));
	}

	public void deleteLeague(Long leagueId) {
		leagueRepository.deleteById(leagueId);
	}
}
