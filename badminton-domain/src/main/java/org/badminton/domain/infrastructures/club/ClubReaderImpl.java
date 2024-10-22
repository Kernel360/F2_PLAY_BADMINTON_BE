package org.badminton.domain.infrastructures.club;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.club.ClubReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClubReaderImpl implements ClubReader {
	private final ClubRepository clubRepository;

	@Override
	public Page<Club> readAllClubs(Pageable pageable) {
		return clubRepository.findAllByIsClubDeletedIsFalse(pageable);
	}

	@Override
	public Club readClub(Long id) {
		return clubRepository.findById(id).orElse(null);
	}
}
