package org.badminton.api.club.provider;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClubDataProvider {

	private final ClubRepository clubRepository;

	public void saveClub(ClubEntity clubEntity) {
		clubRepository.save(clubEntity);
	}

	public void checkIfClubPresent(String clubName) {
		clubRepository.findByClubName(clubName).ifPresent(club -> {
			throw new IllegalArgumentException("이미 존재하는 동호회입니다.");
		});
	}

}
