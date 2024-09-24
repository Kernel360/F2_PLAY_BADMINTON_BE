package org.badminton.api.club.service;

import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.model.dto.ClubDeleteResponse;
import org.badminton.api.club.model.dto.ClubReadResponse;
import org.badminton.api.club.model.dto.ClubUpdateRequest;
import org.badminton.api.club.model.dto.ClubUpdateResponse;
import org.badminton.api.club.validator.ClubValidator;
import org.badminton.domain.club.entity.ClubEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {

	private final ClubValidator clubValidator;

	public ClubReadResponse readClub(Long clubId) {
		ClubEntity club = clubValidator.provideClubByClubId(clubId);
		return ClubReadResponse.clubEntityToClubReadResponse(club);
	}

	// TODO: clubAddRequest에 이미지가 없으면 default 이미지를 넣어주도록 구현
	@Transactional
	public ClubCreateResponse createClub(ClubCreateRequest clubAddRequest) {
		clubValidator.checkIfClubNameDuplicate(clubAddRequest.clubName());
		ClubEntity club = new ClubEntity(clubAddRequest.clubName(), clubAddRequest.clubDescription(),
			clubAddRequest.clubImage());
		clubValidator.saveClub(club);
		return ClubCreateResponse.clubEntityToClubCreateResponse(club);
	}

	@Transactional
	public ClubUpdateResponse updateClub(ClubUpdateRequest clubUpdateRequest, Long clubId) {
		ClubEntity club = clubValidator.provideClubByClubId(clubId);
		club.updateClub(clubUpdateRequest.clubName(), clubUpdateRequest.clubDescription(),
			clubUpdateRequest.clubDescription());
		clubValidator.saveClub(club);
		return ClubUpdateResponse.clubEntityToClubUpdateResponse(club);
	}

	@Transactional
	public ClubDeleteResponse deleteClub(Long clubId) {
		ClubEntity club = clubValidator.provideClubByClubId(clubId);
		club.doWithdrawal();
		return ClubDeleteResponse.clubEntityToClubDeleteResponse(club);
	}

}