package org.badminton.api.member.oauth2;

import java.io.IOException;
import java.util.List;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	@Value("${custom.server.front}")
	private String frontUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		log.info("CustomSuccessHandler onAuthenticationSuccess");

		CustomOAuth2Member customUserDetails = (CustomOAuth2Member)authentication.getPrincipal(); // 인증된 사용자 객체 가져오기

		Long memberId = customUserDetails.getMemberId();
		MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));

		memberEntity.updateLastConnection();

		String registrationId = customUserDetails.getRegistrationId();

		// String oAuthAccessToken = customUserDetails.getOAuthAccessToken();

		List<String> roles = customUserDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.toList();

		/*
		1. Jwt 토큰을 두 개로 나누어서 관리해도 될 지 궁금합니다

			첫 번째 토큰은 사용자의 정보를 불러올 수 있고, 두 번째 토큰은 oAuth API 기능을 사용하기 위한 토큰입니다.
			하나의 JWT 토큰에 사용자의 정보와 oAuthAccessToken 을 넣으면 토큰의 정보의 양이 과해지고, 두 액세스 토큰을 재발급하기 위한
			데이터가 다르기 때문입니다.

		2. 액세스 토큰을 쿠키에 저장하고, 리프레시 토큰을 DB 에 저장해 관리 해도 될 지 궁금합니다.

		 */

		String accessToken = jwtUtil.createCustomAccessToken(String.valueOf(memberId), roles);
		String oAuthToken = jwtUtil.createOAuthToken(customUserDetails.getOAuthAccessToken(), registrationId);

		log.info("Extracted from JWT - registrationId: {}, oAuthAccessToken: {}",
			jwtUtil.getRegistrationId(oAuthToken), jwtUtil.getOAuthAccessToken(oAuthToken));

		String customRefreshToken = jwtUtil.createCustomRefreshToken(String.valueOf(memberId));

		memberEntity.updateRefreshToken(customRefreshToken);
		memberRepository.save(memberEntity);

		clearSession(request, response);

		// jwtUtil.setAccessTokenHeader(response, accessToken);
		jwtUtil.setCustomAccessTokenCookie(response, accessToken);
		jwtUtil.setOAuthTokenCookie(response, oAuthToken);
		// jwtUtil.setRefreshTokenCookie(response, refreshToken);

		response.sendRedirect(frontUrl);

	}

	private void clearSession(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		ResponseCookie jSessionIdCookie = ResponseCookie.from("JSESSIONID", "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.build();
		response.addHeader("Set-Cookie", jSessionIdCookie.toString());
	}
}


