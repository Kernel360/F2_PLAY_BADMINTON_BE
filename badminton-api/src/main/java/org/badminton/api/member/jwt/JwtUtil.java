package org.badminton.api.member.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	//TODO: refresh 토큰 추가

	private final SecretKey secretKey;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {

		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()); // 비밀 키 생성
	}

	public String extractProviderIdFromRequest(HttpServletRequest request) {
		String JwtToken = extractJwtTokenFromRequest(request);
		return getProviderId(JwtToken);
	}

	public String extractJwtTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("JWT".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new NotFoundException(ErrorCode.NOT_FOUND, request.getClass().getSimpleName(), request.getRequestURI());
	}

	public String getProviderId(String token) {
		return getDetail(token, "providerId");
	}

	public String getEmail(String token) {
		return getDetail(token, "email");
	}

	public String getProfileImage(String token) {
		return getDetail(token, "profileImage");
	}

	public String getName(String token) {
		return getDetail(token, "name");
	}

	public String getAuthorization(String token) {
		return getDetail(token, "authorization");
	}

	public String getAccessToken(String token) {
		return getDetail(token, "accessToken");
	}

	public String getRegistrationId(String token) {
		return getDetail(token, "registrationId");
	}

	public String getDetail(String token, String detail) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(detail, String.class);
	}

	public Boolean isExpired(String token) {

		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	public String createJwt(String providerId, String authorization, String name, String email, String profileImage
		, String accessToken, String registrationId, Long expiredMs) {

		return Jwts.builder()
			.claim("providerId", providerId)
			.claim("authorization", authorization)
			.claim("name", name)
			.claim("email", email) // providerId, role, name, email 을 클레임(데이터)로 추가
			.claim("profileImage", profileImage)
			.claim("accessToken", accessToken)
			.claim("registrationId", registrationId)
			.issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시각 설정
			.expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시각 설정 (현재 시각 + expiredMs)
			.signWith(secretKey) // secretKey 로 서명
			.compact(); // Jwt 를 문자열로 변환
	}

}
