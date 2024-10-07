package org.badminton.api.clubmember.controller;

import java.util.List;

import org.badminton.api.clubmember.model.dto.ClubMemberJoinResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberResponse;
import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clubs/{clubId}/clubMembers")
public class ClubMemberController {
	private final ClubMemberService clubMemberService;

	@Operation(summary = "동호회 회원 전체 조회",
		description = "동호회에 가입한 회원들의 리스트를 조회합니다.",
		tags = {"ClubMember"})
	@GetMapping
	public ResponseEntity<List<ClubMemberResponse>> getClubMembersInClub(
		@PathVariable Long clubId
	) {
		List<ClubMemberResponse> clubMemberResponseList = clubMemberService.findAllClubMembers(clubId);
		return ResponseEntity.ok(clubMemberResponseList);
	}

	@Operation(summary = "동호회 가입 신청",
		description = "동호회에 가입을 신청합니다.",
		tags = {"ClubMember"})
	@PostMapping
	public ResponseEntity<ClubMemberJoinResponse> joinClub(Authentication authentication,
		@Parameter(description = "동호회 ID", example = "1") @RequestParam Long clubId) {

		CustomOAuth2Member member = (CustomOAuth2Member)authentication.getPrincipal();
		Long memberId = member.getMemberId();

		ClubMemberJoinResponse clubMemberJoinResponse = clubMemberService.joinClub(memberId, clubId);

		return ResponseEntity.ok(clubMemberJoinResponse);

	}
}

