package org.badminton.api.member.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	private final SecretKey secretKey;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {

		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm()); // 비밀 키 생성
	}

	public String getProviderId(String token) {

		return Jwts.parser()
			.verifyWith(secretKey) // secretKey 를 사용해서 JWT 서명을 검증
			.build()
			.parseSignedClaims(token) // 서명된 JWT 를 파싱하고, 데이터(클레임)를 추출
			.getPayload()
			.get("providerId", String.class); // "providerId" 클레임을 String 으로 변환
	}

	public String getEmail(String token) {

		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("email", String.class);
	}

	public String getName(String token) {

		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("name", String.class);
	}

	public String getRole(String token) {

		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("role", String.class);
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

	public String createJwt(String providerId, String role, String name, String email, Long expiredMs) {

		return Jwts.builder()
			.claim("providerId", providerId)
			.claim("role", role)
			.claim("name", name)
			.claim("email", email) // providerId, role, name, email 을 클레임(데이터)로 추가
			.issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시각 설정
			.expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시각 설정 (현재 시각 + expiredMs)
			.signWith(secretKey) // secretKey 로 서명
			.compact(); // Jwt 를 문자열로 변환
	}

}
