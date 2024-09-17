package org.badminton.api.club.service;

import org.badminton.api.club.model.dto.ClubAddRequest;
import org.badminton.api.club.model.dto.ClubAddResponse;
import org.badminton.api.club.provider.ClubDataProvider;
import org.badminton.domain.club.entity.ClubEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubAddService {

	private final ClubDataProvider clubDataProvider;

	// TODO: clubAddRequest에 이미지가 없으면 default 이미지를 넣어주도록 구현
	public ClubAddResponse addClub(ClubAddRequest clubAddRequest) {
		clubDataProvider.checkIfClubPresent(clubAddRequest.getClubName());
		ClubEntity club = new ClubEntity(clubAddRequest.getClubName(), clubAddRequest.getClubDescription(),
			clubAddRequest.getClubImage());
		clubDataProvider.saveClub(club);
		return ClubAddResponse.toClubAddResponse(club);
	}

}
