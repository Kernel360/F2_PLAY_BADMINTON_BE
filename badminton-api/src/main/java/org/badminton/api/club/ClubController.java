package org.badminton.api.club;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club")
public class ClubController {

	private final ClubRepository clubRepository;

	@GetMapping("")
	public String save() {
		var club = new ClubEntity();
		clubRepository.save(club);
		return "안녕";
	}
}
