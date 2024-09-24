package org.badminton.api.club.validator;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.club.ClubNameDuplicateException;
import org.badminton.api.common.exception.club.ClubNotExistException;
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

	public void checkIfClubNameDuplicate(String clubName) {
		clubRepository.findByClubNameAndIsClubDeletedFalse(clubName).ifPresent(club -> {
			throw new ClubNameDuplicateException(ErrorCode.CLUB_NAME_ALREADY_EXIST, clubName);
		});
	}

	public ClubEntity provideClubByClubId(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(
				() -> new ClubNotExistException(ErrorCode.RESOURCE_NOT_EXIST, clubId));
	}
}
