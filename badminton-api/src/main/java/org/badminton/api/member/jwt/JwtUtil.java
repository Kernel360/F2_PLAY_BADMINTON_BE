package org.badminton.api.member.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	private final SecretKey secretKey;
	private static final long ACCESS_TOKEN_EXPIRY = 60 * 60 * 1000L; // 1시간
	private static final long REFRESH_TOKEN_EXPIRY = 14 * 24 * 60 * 60 * 1000L; // 14일

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()); // 비밀 키 생성
	}

	public String createAccessToken(String memberId, String roles, String registrationId) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("roles", roles)
			.claim("registrationId", registrationId)
			.issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시각 설정
			.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY)) // 만료 시각 설정 (현재 시각 + expiredMs)
			.signWith(secretKey) // secretKey 로 서명
			.compact(); // Jwt 를 문자열로 변환
	}

	public String createRefreshToken(String memberId, String roles, String registrationId) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("roles", roles)
			.claim("registrationId", registrationId)
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
			// .secure(true)
			.path("/")
			.maxAge(REFRESH_TOKEN_EXPIRY) // 14일
			.sameSite("Lax")
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public String getMemberId(String token) {
		return getDetail(token, "memberId");
	}

	public String getRegistrationId(String token) {
		return getDetail(token, "registrationId");
	}

	public String getRoles(String token) {
		return getDetail(token, "roles");
	}

	public String getDetail(String token, String details) {
		String Token = deleteTrim(token);
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(Token)
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

	public boolean validateToken(String token) {
		String Token = deleteTrim(token);
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(Token);
			return !isTokenExpired(Token);
		} catch (Exception e) {
			log.error("Invalid JWT token: {}", e.getMessage());
			return false;
		}
	}

	public Boolean isTokenExpired(String token) {
		String Token = deleteTrim(token);
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(Token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	private String deleteTrim(String token) {
		if (token != null) {
			token = token.trim();
		}
		return token;
	}
}
