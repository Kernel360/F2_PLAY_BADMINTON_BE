package org.badminton.api.league.service;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueStatusUpdateRequest;
import org.badminton.api.league.model.dto.LeagueStatusUpdateResponse;
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
		LeagueEntity savedLeague = leagueRepository.save(
			LeagueCreateRequest.leagueCreateRequestToEntity(createRequest));
		return LeagueCreateResponse.leagueCreateEntityToResponse(savedLeague);
	}

	//TODO : customException 완성 시 해당 Exception 으로 throw 하는 것으로 수정
	public LeagueReadResponse getLeague(Long leagueId) {
		LeagueEntity findResult = leagueRepository.findById(leagueId).orElseThrow();
		return LeagueReadResponse.leagueReadEntityToResponse(findResult);
	}

	public LeagueStatusUpdateResponse updateLeagueStatus(LeagueStatusUpdateRequest updateRequest) {
		LeagueEntity findLeague = leagueRepository.findById(updateRequest.leagueId()).orElseThrow();
		LeagueEntity updatedLeague = LeagueStatusUpdateRequest.leagueStatusToEntity(findLeague, updateRequest);
		return new LeagueStatusUpdateResponse(leagueRepository.save(updatedLeague));
	}

	public void deleteLeague(Long leagueId) {
		leagueRepository.deleteById(leagueId);
	}
}
