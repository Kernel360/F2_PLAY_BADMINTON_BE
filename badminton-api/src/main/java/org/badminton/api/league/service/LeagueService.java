package org.badminton.api.league.service;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.validator.LeagueValidator;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepository;
	private final LeagueValidator leagueValidator;

	public LeagueCreateResponse createLeague(LeagueCreateRequest createRequest) {
		leagueValidator.checkIfLeagueExists(createRequest.leagueName());
		LeagueEntity savedLeague = leagueRepository.save(
			LeagueCreateRequest.leagueCreateRequestToEntity(createRequest));
		return LeagueCreateResponse.leagueCreateEntityToResponse(savedLeague);
	}
}
