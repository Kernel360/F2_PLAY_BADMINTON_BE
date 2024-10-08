package org.badminton.api.member.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
	@Value("${custom.server.domain}")
	private String domain;

	private final SecretKey secretKey;
	private static final long ACCESS_TOKEN_EXPIRY = 60 * 60 * 1000L; // 1시간
	private static final long REFRESH_TOKEN_EXPIRY = 14 * 24 * 60 * 60 * 1000L; // 14일

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()); // 비밀 키 생성
	}

	public String createAccessToken(String memberId, List<String> roles, String registrationId, String oAuthAccessToken) {

		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("roles", String.join(",", roles)) // List<String>을 String으로 변환하여 JWT에 추가
			.claim("registrationId", registrationId)
			.claim("oAuthAccessToken", oAuthAccessToken)
			.issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시각 설정
			.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY)) // 만료 시각 설정 (현재 시각 + expiredMs)
			.signWith(secretKey) // secretKey 로 서명
			.compact(); // Jwt 를 문자열로 변환
	}

	public String createRefreshToken(String memberId, List<String> roles, String registrationId) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("roles", String.join(",", roles)) // List<String>을 String으로 변환하여 JWT에 추가
			.claim("registrationId", registrationId)
			// .claim("oAuthAccessToken", oAuthAccessToken)
			.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
			.signWith(secretKey)
			.compact();
	}

	public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
		response.setHeader("Authorization", "Bearer %s".formatted(accessToken));
	}

	public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.domain(domain)
			.path("/")
			.maxAge(REFRESH_TOKEN_EXPIRY) // 14일
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
		ResponseCookie cookie = ResponseCookie.from("access_token", accessToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.domain(domain)
			.path("/")
			.maxAge(ACCESS_TOKEN_EXPIRY) // 14일
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public String getMemberId(String token) {
		return getDetail(token, "memberId");
	}

	public String getRegistrationId(String token) {
		return getDetail(token, "registrationId");

	}

	public String getOAuthToken(String token) {
		return getDetail(token, "oAuthAccessToken");
	}
	public List<String> getRoles(String token) {
		String rolesString = getDetail(token, "roles");
		return (rolesString != null) ? List.of(rolesString.split(",")) : Collections.emptyList(); // 문자열을 List로 변환
	}

	public String getDetail(String token, String details) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(details, String.class);
	}

	public String extractAccessTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public String extractRefreshTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("refresh_token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public String extractAccessTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("access_token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
			return false;
		} catch (Exception e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			return false;
		}
	}
}
