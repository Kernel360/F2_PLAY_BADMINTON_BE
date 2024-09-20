package org.badminton.api.club.controller;

import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.service.ClubCreateService;
import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.DuplicationException;
import org.badminton.domain.club.repository.ClubRepository;
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

	private final ClubCreateService clubAddService;
	private final ClubRepository clubRepository;

	@PostMapping
	@Operation(summary = "동호회 추가",
		description = "동호회를 추가합니다.",
		tags = {"Club"})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "클럽이 성공적으로 추가되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
	})
	public ResponseEntity<ClubCreateResponse> createClub(@Valid @RequestBody ClubCreateRequest clubAddRequest) {
		ClubCreateResponse clubAddResponse = clubAddService.createClub(clubAddRequest);
		if (clubAddResponse != null)
			throw new DuplicationException(ErrorCode.RESOURCE_NOT_EXIST, "동호회");
		return ResponseEntity.ok(clubAddResponse);
	}
}