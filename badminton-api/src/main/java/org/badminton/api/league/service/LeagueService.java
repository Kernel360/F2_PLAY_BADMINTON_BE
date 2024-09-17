package org.badminton.api.league.service;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepository;

	public void createLeague(LeagueCreateRequest createRequest) {
		leagueRepository.save(LeagueCreateRequest.createRequestToEntity(createRequest));
	}

}
