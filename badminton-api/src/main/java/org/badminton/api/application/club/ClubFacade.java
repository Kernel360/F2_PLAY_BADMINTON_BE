package org.badminton.api.application.club;

import org.badminton.domain.domain.club.ClubService;
import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubFacade {
	private final ClubService clubService;

	public Page<ClubCardInfo> readAllClubs(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return clubService.readAllClubs(pageable);
	}

	public ClubCreateInfo createClub(ClubCreateCommand createRequest, Long memberId) {
		return clubService.createClub(createRequest, memberId);
	}
}
