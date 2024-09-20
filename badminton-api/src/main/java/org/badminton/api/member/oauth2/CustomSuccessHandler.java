package org.badminton.api.member.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.CustomOAuth2Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		log.info("CustomSuccessHandler onAuthenticationSuccess");

		//OAuth2User
		CustomOAuth2Member customUserDetails = (CustomOAuth2Member)authentication.getPrincipal(); // 인증된 사용자 객체 가져오기

		String providerId = customUserDetails.getProviderId();

		String name = customUserDetails.getName();

		String email = customUserDetails.getEmail();

		String profileImage = customUserDetails.getProfileImage();
		log.info("profileImage: {}", profileImage);

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String authorization = auth.getAuthority();

		String token = jwtUtil.createJwt(providerId, authorization, name, email, profileImage,
			60 * 60 * 60L); // 초 * 분 * 시

		// response.setHeader("Authorization", "Bearer " + token);

		response.addCookie(createCookie(token));
		response.sendRedirect("http://localhost:3000/");
	}

	// JWT 쿠키 생성 메서드
	private Cookie createCookie(String value) {
		Cookie cookie = new Cookie("JWT", value);
		cookie.setMaxAge(60 * 60 * 60);
		//cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 이 쿠키에 접근 못하도록 -> 보안 강화

		return cookie;
	}

}

