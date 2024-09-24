package org.badminton.api.member;

import org.badminton.api.member.model.dto.MemberDeleteResponse;
import org.badminton.api.member.model.dto.MemberLogoutResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.model.dto.MemberUpdateResponse;
import org.badminton.api.member.service.MemberService;
import org.springframework.http.ResponseEntity;
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

	@Operation(
		summary = "프로필 사진을 수정합니다",
		description = "프로필 사진을 수정합니다",
		tags = {"Member"}
	)
	@PatchMapping
	public ResponseEntity<MemberUpdateResponse> update(HttpServletRequest request,
		@RequestBody MemberUpdateRequest updateRequest) {
		MemberUpdateResponse memberUpdateResponse = memberService.updateMember(request, updateRequest);
		return ResponseEntity.ok(memberUpdateResponse);
	}

	@Operation(
		summary = "회원 탈퇴를 합니다",
		description = "멤버 필드의 isDeleted 를 true 로 변경합니다",
		tags = {"Member"}
	)
	@DeleteMapping
	public ResponseEntity<MemberDeleteResponse> delete(HttpServletRequest request, HttpServletResponse response) {
		MemberDeleteResponse memberDeleteResponse = memberService.deleteMember(request, response);
		return ResponseEntity.ok(memberDeleteResponse);
	}

	@Operation(
		summary = "로그아웃을 합니다",
		description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
		tags = {"Member"}
	)
	@PostMapping("/logout")
	public ResponseEntity<MemberLogoutResponse> logout(HttpServletRequest request, HttpServletResponse response) {
		MemberLogoutResponse memberLogoutResponse = memberService.logoutMember(request, response);
		return ResponseEntity.ok(memberLogoutResponse);
	}

}
