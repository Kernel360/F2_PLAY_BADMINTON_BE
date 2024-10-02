package org.badminton.api.member.controller;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberDeleteResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.model.dto.MemberUpdateResponse;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.api.member.service.MemberService;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/member")
@Slf4j
public class MemberController {

	private final MemberService memberService;
	private final JwtUtil jwtUtil;
	private final ClubMemberRepository clubMemberRepository;

	@Operation(
		summary = "액세스 토큰을 재발급합니다",
		description = "리프레시 토큰을 이용해서 액세스 토큰을 재발급합니다",
		tags = {"Member"}
	)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		return memberService.refreshToken(request, response);
	}

	@Operation(
		summary = "로그아웃을 합니다",
		description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
		tags = {"Member"}
	)
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletResponse response,
		@AuthenticationPrincipal CustomOAuth2Member member) {
		log.info("Logout request received");
		memberService.logoutMember(member.getMemberId(), response);
		return ResponseEntity.ok("Logged out successfully");
	}

	@Operation(
		summary = "프로필 사진을 수정합니다",
		description = "프로필 사진을 수정합니다",
		tags = {"Member"}
	)
	@PatchMapping
	public ResponseEntity<MemberUpdateResponse> update(
		@RequestBody MemberUpdateRequest updateRequest, @AuthenticationPrincipal CustomOAuth2Member member) {
		return ResponseEntity.ok(memberService.updateMember(member.getMemberId(), updateRequest));
	}

	@Operation(
		summary = "회원 탈퇴를 합니다",
		description = "멤버 필드의 isDeleted 를 true 로 변경합니다",
		tags = {"Member"}
	)
	@DeleteMapping
	public ResponseEntity<MemberDeleteResponse> deleteMember(HttpServletRequest request, HttpServletResponse response,
		@AuthenticationPrincipal CustomOAuth2Member member) {
		MemberDeleteResponse deleteResponse = memberService.deleteMember(member, request, response);
		return ResponseEntity.ok(deleteResponse);
	}

}
