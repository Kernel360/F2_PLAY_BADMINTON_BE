package org.badminton.api.member.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
	private final int EXPIRED_TIME = 60 * 60 * 60;
	private final MemberRepository memberRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		log.info("CustomSuccessHandler onAuthenticationSuccess");

		//OAuth2User
		CustomOAuth2Member customUserDetails = (CustomOAuth2Member)authentication.getPrincipal(); // 인증된 사용자 객체 가져오기

		String memberId = String.valueOf(customUserDetails.getMemberId());

		String providerId = customUserDetails.getProviderId();

		String name = customUserDetails.getName();

		String email = customUserDetails.getEmail();

		String profileImage = customUserDetails.getProfileImage();
		log.info("profileImage: {}", profileImage);

		String accessToken = customUserDetails.getAccessToken();
		String registrationId = customUserDetails.getRegistrationId();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String authorization = auth.getAuthority();

		MemberEntity memberEntity = memberRepository.findByMemberId(customUserDetails.getMemberId())
			.orElseThrow(() -> new MemberNotExistException(String.valueOf(customUserDetails.getMemberId())));

		memberEntity.updateLastConnection();

		String token = jwtUtil.createJwt(memberId, providerId, authorization, name, email, profileImage,
			accessToken, registrationId, 24 * 60 * 60 * 1000L); // 초 * 분 * 시

		request.getSession().invalidate();

		response.setHeader("Set-Cookie", "JSESSIONID=; HttpOnly; Path=/; Max-Age=0; SameS ite=None;");

		response.addCookie(createCookie(token));

		// response.sendRedirect("http://localhost:3000?token=" + token + "&expires=" + EXPIRED_TIME);
		response.sendRedirect("http://localhost:3000");

	}

	// JWT 쿠키 생성 메서드
	private Cookie createCookie(String value) {
		Cookie cookie = new Cookie("JWT", value);
		cookie.setMaxAge(EXPIRED_TIME);  // 쿠키 유효시간 설정
		// cookie.setSecure(false);
		cookie.setPath("/");  // 모든 경로에 대해 쿠키를 유효하게 설정
		// cookie.setHttpOnly(true);  // 자바스크립트로 접근 불가능하게 설정
		return cookie;
	}

}


