package org.badminton.api.interfaces.club.controller;

import org.badminton.api.application.club.ClubFacade;
import org.badminton.api.interfaces.club.dto.ClubCardResponse;
import org.badminton.api.interfaces.club.dto.ClubCreateRequest;
import org.badminton.api.interfaces.club.dto.ClubCreateResponse;
import org.badminton.api.interfaces.club.dto.ClubDeleteResponse;
import org.badminton.api.interfaces.club.dto.ClubDetailsResponse;
import org.badminton.api.interfaces.club.dto.ClubUpdateRequest;
import org.badminton.api.interfaces.club.dto.ClubUpdateResponse;
import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
import org.badminton.api.service.club.ClubService;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
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

	private final ClubFacade clubFacade;
	private final ClubService clubService;
	private static final String DEFAULT_PAGE_VALUE = "0";
	private static final String DEFAULT_SIZE_VALUE = "9";
	private static final String DEFAULT_SORT_BY_VALUE = "clubId";

	private final ClubMemberRepository clubMemberRepository;

	@GetMapping("/{clubId}")
	@Operation(summary = "동호회 조회",
		description = "동호회를 조회합니다.",
		tags = {"Club"})
	public ResponseEntity<ClubDetailsResponse> readClub(@PathVariable Long clubId,
		@AuthenticationPrincipal(errorOnInvalidType = false) CustomOAuth2Member member) {
		Long memberId = member != null ? member.getMemberId() : null;
		ClubDetailsResponse clubDetailsResponse = clubService.readClub(clubId, memberId);
		return ResponseEntity.ok(clubDetailsResponse);
	}

	@PatchMapping("{clubId}")
	@Operation(summary = "동호회 수정",
		description = """
			새로운 동호회를 수정합니다. 다음 조건을 만족해야 합니다:
						
			1. 동호회 이름:
			   - 필수 입력
			   - 2자 이상 20자 이하
						
			2. 동호회 소개:
			   - 2자 이상 1000자 이하
						
			3. 동호회 이미지 URL:
			   - 호스트: badminton-team.s3.ap-northeast-2.amazonaws.com
			   - 경로: /club-banner/로 시작
			   - 파일 확장자: png, jpg, jpeg, gif 중 하나""",
		tags = {"Club"}
	)
	public ResponseEntity<ClubUpdateResponse> updateClub(@PathVariable Long clubId,
		@Valid @RequestBody ClubUpdateRequest clubUpdateRequest) {
		ClubUpdateResponse clubUpdateResponse = clubService.updateClub(clubUpdateRequest, clubId);
		return ResponseEntity.ok(clubUpdateResponse);
	}

	@PostMapping
	@Operation(summary = "동호회 추가",
		description = """
			새로운 동호회를 생성합니다. 다음 조건을 만족해야 합니다:
			1. 동호회 이름:
			   - 필수 입력
			   - 2자 이상 20자 이하
						
			2. 동호회 소개:
			   - 2자 이상 1000자 이하
						
			3. 동호회 이미지 URL:
			   - 호스트: badminton-team.s3.ap-northeast-2.amazonaws.com
			   - 경로: /club-banner/로 시작
			   - 파일 확장자: png, jpg, jpeg, gif 중 하나
			   - https://d36om9pjoifd2y.cloudfront.net/club-banner/804a0dfc-947f-4039-acbe-d95a85893087.png
			""", tags = {"Club"}
	)
	public ResponseEntity<ClubCreateResponse> createClub(@Valid @RequestBody ClubCreateRequest clubCreateRequest,
		@AuthenticationPrincipal CustomOAuth2Member member) {
		Long memberId = member.getMemberId();
		ClubCreateResponse clubAddResponse = clubService.createClub(clubCreateRequest, memberId);
		return ResponseEntity.ok(clubAddResponse);
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
	public ResponseEntity<Page<ClubCardInfo>> readAllClub(
		@RequestParam(defaultValue = DEFAULT_PAGE_VALUE) int page,
		@RequestParam(defaultValue = DEFAULT_SIZE_VALUE) int size,
		@RequestParam(defaultValue = DEFAULT_SORT_BY_VALUE) String sort) {
		return ResponseEntity.ok(clubFacade.readAllClubs(page, size, sort));
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