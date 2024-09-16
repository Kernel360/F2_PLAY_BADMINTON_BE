package org.badminton.api.club;

import org.badminton.api.club.model.dto.ClubJoinResponse;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<ClubJoinResponse> save() {
		var club = new ClubEntity("박소은", "소은소은");
		clubRepository.save(club);
		ClubJoinResponse clubJoinResponse = ClubJoinResponse.builder()
			.name(club.getClubName())
			.description(club.getClubDescription())
			.createdAt(club.getCreatedAt())
			.modifiedAt(club.getModifiedAt())
			.build();
		return ResponseEntity.ok(clubJoinResponse);
	}
}