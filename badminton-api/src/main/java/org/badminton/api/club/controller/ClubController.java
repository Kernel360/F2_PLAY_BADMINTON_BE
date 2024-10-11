package org.badminton.api.club.controller;

import java.util.List;

import org.badminton.api.club.model.dto.ClubCardResponse;
import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.model.dto.ClubDeleteResponse;
import org.badminton.api.club.model.dto.ClubDetailsResponse;
import org.badminton.api.club.model.dto.ClubUpdateRequest;
import org.badminton.api.club.model.dto.ClubUpdateResponse;
import org.badminton.api.club.service.ClubService;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clubs")
public class ClubController {

	private final ClubService clubService;
	private static final String DEFAULT_PAGE_VALUE = "0";
	private static final String DEFAULT_SIZE_VALUE = "9";
	private static final String DEFAULT_SORT_BY_VALUE = "clubId";

	private final ClubMemberRepository clubMemberRepository;

	@GetMapping("/{clubId}")
	@Operation(summary = "동호회 조회",
		description = "동호회를 조회합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubDetailsResponse> readClub(@PathVariable Long clubId) {
		ClubDetailsResponse clubDetailsResponse = clubService.readClub(clubId);
		return ResponseEntity.ok(clubDetailsResponse);
	}

	@GetMapping("/me")
	@Operation(summary = "현재 로그인된 사용자의 동호회 조회", description = "현재 로그인되어 있는 사용자의 동호회를 조회합니다", tags = {"Club"})
	public ResponseEntity<ClubDetailsResponse> readCurrentClub(@AuthenticationPrincipal CustomOAuth2Member member) {
		ClubDetailsResponse clubDetailsResponse = clubService.readCurrentClub(member.getMemberId());
		return ResponseEntity.ok(clubDetailsResponse);
	}

	@PostMapping()
	@Operation(summary = "동호회 추가",
		description = "동호회를 추가합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubCreateResponse> createClub(@Valid @RequestBody ClubCreateRequest clubCreateRequest,
		@AuthenticationPrincipal CustomOAuth2Member member) {
		Long memberId = member.getMemberId();
		ClubCreateResponse clubAddResponse = clubService.createClub(clubCreateRequest, memberId);
		return ResponseEntity.ok(clubAddResponse);
	}

	@PatchMapping("{clubId}")
	@Operation(summary = "동호회 수정",
		description = "동호회를 수정합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubUpdateResponse> updateClub(@PathVariable Long clubId,
		@Valid @RequestBody ClubUpdateRequest clubUpdateRequest) {
		ClubUpdateResponse clubUpdateResponse = clubService.updateClub(clubUpdateRequest, clubId);
		return ResponseEntity.ok(clubUpdateResponse);
	}

	@DeleteMapping("{clubId}")
	@Operation(summary = "동호회 삭제",
		description = "동호회를 삭제합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubDeleteResponse> deleteClub(@PathVariable Long clubId) {
		ClubDeleteResponse clubDeleteResponse = clubService.deleteClub(clubId);
		return ResponseEntity.ok(clubDeleteResponse);
	}

	@GetMapping()
	@Operation(summary = "전체 동호회 조회",
		description = "전체 동호회를 조회합니다.",
		tags = {"Club"})

	public ResponseEntity<Page<ClubCardResponse>> readAllClub(
		@RequestParam(defaultValue = DEFAULT_PAGE_VALUE) int page,
		@RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size,
		@RequestParam(defaultValue = DEFAULT_SORT_BY_VALUE) String sort) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return ResponseEntity.ok(clubService.readAllClubs(pageable));
	}

	@Operation(summary = "검색 조건에 맞는 동호회 조회",
		description = "검색 조건에 맞는 동호회를 조회합니다.",
		tags = {"Club"})
	@GetMapping("/search")
	public ResponseEntity<Page<ClubCardResponse>> clubSearch(
		@RequestParam(defaultValue = DEFAULT_PAGE_VALUE) int page,
		@RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size,
		@RequestParam(defaultValue = DEFAULT_SORT_BY_VALUE) String sort,
		@RequestParam(required = false) String keyword) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return ResponseEntity.ok(clubService.searchClubs(keyword, pageable));

	}

}