package org.badminton.api.member;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/member")
@Slf4j
public class MemberController {

	//TODO: 리팩토링

	private final MemberService memberService;
	private final JwtUtil jwtUtil;
	@Value("${NAVER_CLIENT_ID}")
	private String naverClientId;

	@Value("${NAVER_CLIENT_SECRET}")
	private String naverClientSecret;

	@Operation(
		summary = "로그아웃을 합니다",
		description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
		tags = {"Member"}
	)
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

		String jwtToken = jwtUtil.extractJwtTokenFromRequest(request);
		if (jwtToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Jwt 토큰을 찾을 수 없습니다");
		}

		String accessToken = jwtUtil.getAccessToken(jwtToken);
		String registrationId = jwtUtil.getRegistrationId(jwtToken);

		if ("google".equals(registrationId)) {
			googleUnlink(accessToken);
		} else if ("naver".equals(registrationId)) {
			naverUnlink(accessToken);
		} else if ("kakao".equals(registrationId)) {
			kakaoUnlink(accessToken);
		}

		Cookie cookie = new Cookie("JWT", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);

		return ResponseEntity.ok("로그아웃 성공 , OAuth 연결끊기 성공!");
	}

	private void naverUnlink(String accessToken) {
		String revokeUrl = String.format(
			"https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=NAVER",
			naverClientId, naverClientSecret, accessToken);
		try {
			URL url = new URL(revokeUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Length", "0");

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				log.info("Naver account successfully unlinked");
			} else {
				log.error("Failed to unlink Naver account, response code: {}", responseCode);
			}
		} catch (IOException e) {
			log.error("Error occurred while unlinking Naver account", e);
		}
	}

	private void kakaoUnlink(String accessToken) {
		String revokeUrl = "https://kapi.kakao.com/v1/user/unlink";
		try {
			URL url = new URL(revokeUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				log.info("Kakao account successfully unlinked");
			} else {
				log.error("Failed to unlink Kakao account, response code: {}", responseCode);
			}
		} catch (IOException e) {
			log.error("Error occurred while unlinking Kakao account", e);
		}
	}

	private void googleUnlink(String accessToken) {
		String revokeUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
		try {
			URL url = new URL(revokeUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET"); // GET 요청으로 변경
			connection.setRequestProperty("Content-Length", "0"); // Content-Length 설정

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				log.info("Google account successfully unlinked");
			} else {
				log.error("Failed to unlink Google account, response code: {}", responseCode);
			}
		} catch (IOException e) {
			log.error("Error occurred while unlinking Google account", e);
		}
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
