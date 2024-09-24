package org.badminton.api.member.jwt;

import java.io.IOException;

import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.member.entity.MemberAuthorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/") || path.equals("/groups") || path.startsWith("/oauth2") || path.startsWith("/login")
			|| path.startsWith("/api") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");

	}

	// 일반적인 예외 -> 커스텀 예외 처리 X
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String jwtToken = jwtUtil.extractJwtTokenFromRequest(request);

		if (jwtToken == null) {
			log.info("JWT cookie not found");
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtUtil.isExpired(jwtToken)) {
			log.info("JWT token expired");
			Cookie expiredCookie = new Cookie("JWT", null);
			expiredCookie.setMaxAge(0);
			expiredCookie.setPath("/");
			response.addCookie(expiredCookie);

			filterChain.doFilter(request, response);

			return;
		}

		String providerId = jwtUtil.getProviderId(jwtToken);
		String authorization = jwtUtil.getAuthorization(jwtToken);
		log.info("JWT authorization: {}", authorization);
		Long memberId = Long.valueOf(jwtUtil.getMemberId(jwtToken));
		String name = jwtUtil.getName(jwtToken);
		String email = jwtUtil.getEmail(jwtToken);
		String profileImage = jwtUtil.getProfileImage(jwtToken);
		String accessToken = jwtUtil.getAccessToken(jwtToken);
		String registrationId = jwtUtil.getRegistrationId(jwtToken);

		MemberResponse memberResponse = new MemberResponse(memberId, MemberAuthorization.AUTHORIZATION_USER.name(),
			name,
			email,
			providerId, profileImage);
		log.info("memberDto: {}", memberResponse);

		CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberResponse, accessToken, registrationId);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null,
			customOAuth2Member.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);

	}
}
