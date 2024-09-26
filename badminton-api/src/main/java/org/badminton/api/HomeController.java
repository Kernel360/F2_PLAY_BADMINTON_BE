package org.badminton.api;

import java.util.Map;

import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {

	@GetMapping("/")
	@ResponseBody
	public String home() {
		return "This is the index page";
	}

	@GetMapping("/myPage")
	public ResponseEntity<Object> myPage(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			CustomOAuth2Member member = (CustomOAuth2Member)authentication.getPrincipal();
			return ResponseEntity.ok(member); // 200 OK 상태 코드로 응답
		} else {
			Map<String, String> response = Map.of("message", "Guest, 회원가입한 유저만 접속 가능합니다.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized 상태 코드로 응답
		}
	}

}
