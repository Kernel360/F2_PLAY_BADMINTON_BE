package org.badminton.api.club.validator;

import static org.badminton.api.common.error.ClubErrorCode.*;

import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClubValidator {

	private final ClubRepository clubRepository;

	public void saveClub(ClubEntity clubEntity) {
		clubRepository.save(clubEntity);
	}

	public void checkIfClubPresent(String clubName) {
		clubRepository.findByClubName(clubName).ifPresent(club -> {
			throw new BadmintonException(CLUB_ALREADY_EXISTS);
		});
	}

}
