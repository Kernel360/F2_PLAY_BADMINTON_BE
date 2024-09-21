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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/member")
public class MemberController {

	private final MemberService memberService;

	@Operation(
		summary = "로그아웃을 합니다",
		description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
		tags = {"Member"}
	)
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("JWT", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		if (request.getSession(false) != null) {
			request.getSession().invalidate();
		}

		return new ResponseEntity<>("Logout successful", HttpStatus.OK);
	}

	@Operation(
		summary = "프로필 사진을 수정합니다",
		description = "프로필 사진을 수정합니다",
		tags = {"Member"}

	)
	@PatchMapping
	public ResponseEntity<String> update(HttpServletRequest request, @RequestBody MemberUpdateRequest updateRequest) {
		memberService.updateMember(request, updateRequest);
		return new ResponseEntity<>("update successful", HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(HttpServletRequest request, HttpServletResponse response) {
		return new ResponseEntity<>("delete successful", HttpStatus.OK);
	}

}
