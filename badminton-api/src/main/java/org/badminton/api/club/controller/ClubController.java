package org.badminton.api.club.controller;

import org.badminton.api.club.model.dto.ClubAddRequest;
import org.badminton.api.club.model.dto.ClubAddResponse;
import org.badminton.api.club.service.ClubAddService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club")
public class ClubController {

	private final ClubAddService clubAddService;

	@PostMapping
	public ResponseEntity<ClubAddResponse> addClubController(@Valid @RequestBody ClubAddRequest clubAddRequest) {
		log.info(clubAddRequest.getClubName());
		log.info(clubAddRequest.getClubDescription());

		ClubAddResponse clubAddResponse = clubAddService.addClub(clubAddRequest);
		return ResponseEntity.ok(clubAddResponse);
	}
}