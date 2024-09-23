package org.badminton.api.club.validator;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.DuplicationException;
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
			throw new DuplicationException(ErrorCode.RESOURCE_ALREADY_EXIST, clubName.getClass().getSimpleName(),
				clubName);
		});
	}

	public ClubEntity provideClubByClubId(Long clubId) {
		return clubRepository.findById(clubId).orElseThrow();
	}
}
