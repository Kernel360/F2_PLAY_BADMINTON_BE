package org.badminton.api.clubmember.controller;

import org.badminton.api.clubmember.model.dto.ClubMemberParticipateResponse;
import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clubMember")
public class ClubMemberController {
	private final ClubMemberService clubMemberService;
	private final MemberRepository memberRepository;

	@Operation(summary = "동호회 가입 신청",
		description = "동호회에 가입을 신청합니다.",
		tags = {"ClubMember"})
	@PostMapping
	public ResponseEntity<ClubMemberParticipateResponse> participateClub(Authentication authentication,
		@Parameter(description = "동호회 ID", example = "1") @RequestParam Long clubId) {

		CustomOAuth2Member member = (CustomOAuth2Member)authentication.getPrincipal();
		Long memberId = member.getMemberId();

		ClubMemberEntity clubMemberEntity = clubMemberService.participateClub(memberId, clubId);
		ClubMemberParticipateResponse clubMemberParticipateResponse = ClubMemberParticipateResponse.clubMemberEntityToClubMemberParticipateResponse(
			clubMemberEntity);

		return ResponseEntity.ok(clubMemberParticipateResponse);

	}
}
