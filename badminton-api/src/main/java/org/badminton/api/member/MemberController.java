package org.badminton.api.member;

import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.service.MemberService;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<String> update(HttpServletRequest request,
		@RequestBody MemberUpdateRequest updateRequest) {
		try {
			memberService.updateMember(request, updateRequest);
			return new ResponseEntity<>("update successful", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("update failed", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("update failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Operation(
		summary = "회원 탈퇴를 합니다",
		description = "멤버 필드의 isDeleted 를 true 로 변경합니다",
		tags = {"Member"}
	)
	@DeleteMapping
	public ResponseEntity<String> delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			memberService.deleteMember(request, response);
			return new ResponseEntity<>("delete successful", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(
		summary = "로그아웃을 합니다",
		description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
		tags = {"Member"}
	)
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		memberService.logoutMember(request, response);
		return ResponseEntity.ok("로그아웃 성공 , OAuth 연결끊기 성공!");
	}

}
