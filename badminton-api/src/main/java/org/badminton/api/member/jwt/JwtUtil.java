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
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String createCustomAccessToken(String memberId, List<String> roles) {

		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("roles", String.join(",", roles))
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
			.signWith(secretKey)
			.compact();
	}

	public String createOAuthToken(String oAuthAccessToken, String registrationId) {
		return Jwts.builder()
			.claim("oAuthAccessToken", oAuthAccessToken)
			.claim("registrationId", registrationId)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
			.signWith(secretKey)
			.compact();
	}
	public String createCustomRefreshToken(String memberId) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
			.signWith(secretKey)
			.compact();
	}
	// public String createRefreshToken(String memberId, List<String> roles, String registrationId) {
	// 	return Jwts.builder()
	// 		.claim("memberId", memberId)
	// 		.claim("roles", String.join(",", roles))
	// 		.claim("registrationId", registrationId)
	// 		.expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
	// 		.signWith(secretKey)
	// 		.compact();
	// }

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

	public void setCustomAccessTokenCookie(HttpServletResponse response, String customAccessToken) {
		ResponseCookie cookie = ResponseCookie.from("custom_access_token", customAccessToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.domain(domain)
			.path("/")
			.maxAge(ACCESS_TOKEN_EXPIRY) // 14일
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void setOAuthTokenCookie(HttpServletResponse response, String oAuthToken) {
		ResponseCookie cookie = ResponseCookie.from("oauth_token", oAuthToken)
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

	public String getOAuthAccessToken(String token) {
		return getDetail(token, "oAuthAccessToken");
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

	public String extractOAuthTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("oauth_token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public String extractCustomAccessTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("custom_access_token".equals(cookie.getName())) {
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
