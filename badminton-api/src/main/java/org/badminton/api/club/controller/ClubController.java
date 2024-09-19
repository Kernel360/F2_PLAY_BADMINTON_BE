package org.badminton.api.club.controller;

import org.badminton.api.club.model.dto.ClubAddRequest;
import org.badminton.api.club.model.dto.ClubAddResponse;
import org.badminton.api.club.service.ClubAddService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/club")
public class ClubController {

	private final ClubAddService clubAddService;

	@PostMapping
	@Operation(summary = "동호회 추가",
		description = "동호회를 추가합니다.",
		tags = {"Club"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "클럽이 성공적으로 추가되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	public ResponseEntity<ClubAddResponse> addClubController(@Valid @RequestBody ClubAddRequest clubAddRequest) {
		ClubAddResponse clubAddResponse = clubAddService.addClub(clubAddRequest);
		return ResponseEntity.ok(clubAddResponse);
	}
}