package org.badminton.api.league.service;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueService {
	private final LeagueRepository leagueRepository;

	public void createLeague(LeagueCreateRequest createRequest) {
		leagueRepository.save(createRequestToEntity(createRequest));
		// System.out.println(createRequest.toString());
	}

	private LeagueEntity createRequestToEntity(LeagueCreateRequest request) {
		return new LeagueEntity(request.getName(), request.getDescription(), request.getLeagueAt(),
			request.getTierLimit(), request.getClosedAt(), request.getStatus(), request.getPlayerCount(),
			request.getMatchType(), request.getCreatedAt(), request.getModifiedAt());
	}
}
