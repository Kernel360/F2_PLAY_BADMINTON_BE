package org.badminton.api.club.service;

import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.validator.ClubValidator;
import org.badminton.domain.club.entity.ClubEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubCreateService {

	private final ClubValidator clubDataProvider;

	// TODO: clubAddRequest에 이미지가 없으면 default 이미지를 넣어주도록 구현
	@Transactional
	public ClubCreateResponse createClub(ClubCreateRequest clubAddRequest) {
		clubDataProvider.checkIfClubPresent(clubAddRequest.getClubName());
		ClubEntity club = new ClubEntity(clubAddRequest.getClubName(), clubAddRequest.getClubDescription(),
			clubAddRequest.getClubImage());
		clubDataProvider.saveClub(club);
		return ClubCreateResponse.clubEntityToClubAddResponse(club);
	}

}
