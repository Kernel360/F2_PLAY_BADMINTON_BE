package org.badminton.api.club.controller;

import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/club")
public class ClubController {

	private final ClubService clubService;

	@PostMapping
	@Operation(summary = "동호회 추가",
		description = "동호회를 추가합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubCreateResponse> createClub(@Valid @RequestBody ClubCreateRequest clubAddRequest) {
		ClubCreateResponse clubAddResponse = clubService.createClub(clubAddRequest);
		return ResponseEntity.ok(clubAddResponse);
	}
}