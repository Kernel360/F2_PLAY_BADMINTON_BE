package org.badminton.api.clubmember.controller;

import java.util.List;
import java.util.Map;

import org.badminton.api.clubmember.model.dto.clubMemberBanRecordResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberBanRequest;
import org.badminton.api.clubmember.model.dto.ClubMemberExpelRequest;
import org.badminton.api.clubmember.model.dto.ClubMemberJoinResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberRoleUpdateRequest;
import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/v1/clubs/{clubId}/clubMembers")
public class ClubMemberController {
	private final ClubMemberService clubMemberService;

	@Operation(summary = "동호회 회원 전체 조회",
		description = "동호회에 가입한 회원들의 리스트를 조회합니다.",
		tags = {"ClubMember"})
	@GetMapping
	public ResponseEntity<Map<ClubMemberRole, List<ClubMemberResponse>>> getClubMembersInClub(
		@PathVariable Long clubId
	) {
		Map<ClubMemberRole, List<ClubMemberResponse>> clubMemberResponseList =
			clubMemberService.findAllClubMembers(clubId);

		return ResponseEntity.ok(clubMemberResponseList);
	}

	@Operation(summary = "동호회 가입 신청",
		description = "동호회에 가입을 신청합니다.",
		tags = {"ClubMember"})
	@PostMapping
	public ResponseEntity<ClubMemberJoinResponse> joinClub(@AuthenticationPrincipal CustomOAuth2Member member,
		@Parameter(description = "동호회 ID", example = "1") @PathVariable Long clubId) {

		log.info("member :{}", member);

		Long memberId = member.getMemberId();

		ClubMemberJoinResponse clubMemberJoinResponse = clubMemberService.joinClub(memberId, clubId);

		return ResponseEntity.ok(clubMemberJoinResponse);
	}

	@Operation(summary = "동호회원 역할 변경",
		description = "동호회원의 역할을 변경합니다.",
		tags = {"ClubMember"})
	@PatchMapping("/role")
	public ResponseEntity<ClubMemberResponse> updateClubMemberRole(@RequestBody ClubMemberRoleUpdateRequest request,
		@RequestParam Long clubMemberId, @PathVariable Long clubId) {
		return ResponseEntity.ok(clubMemberService.updateClubMemberRole(request, clubMemberId));
	}

	@Operation(summary = "동호회원 강제 탈퇴시키기",
		description = "동호회원을 강제 탈퇴시킵니다.",
		tags = {"ClubMember"})
	@PatchMapping("/expel")
	public ResponseEntity<clubMemberBanRecordResponse> expelClubMember(@RequestParam Long clubMemberId, @PathVariable Long clubId, @RequestBody
		ClubMemberExpelRequest request) {
		return ResponseEntity.ok(clubMemberService.expelClubMember(request, clubMemberId));
	}

	@Operation(summary = "동호회원 정지",
		description = "동호회원의 활동을 정지시킵니다.",
		tags = {"ClubMember"})
	@PatchMapping("/ban")
	public ResponseEntity<clubMemberBanRecordResponse> banClubMember(@RequestParam Long clubMemberId, @PathVariable Long clubId, @RequestBody ClubMemberBanRequest request) {
		return ResponseEntity.ok(clubMemberService.banClubMember(request, clubMemberId));
	}
}

