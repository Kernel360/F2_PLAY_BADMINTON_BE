package org.badminton.api.member.jwt;

import java.io.IOException;

import org.badminton.api.member.model.dto.CustomOAuth2Member;
import org.badminton.api.member.model.dto.MemberResponse;
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
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return path.equals("/") || path.equals("/groups") || path.startsWith("/oauth2") || path.startsWith("/login")
			|| path.startsWith("/api") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
			|| path.startsWith("/v1");

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String JWT = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			log.info("cookie: {}", cookie.getName());
			if (cookie.getName().equals("JWT")) {
				JWT = cookie.getValue();
			}
		}

		if (JWT == null) {
			log.info("JWT cookie not found");
			filterChain.doFilter(request, response);

			return;
		}

		String token = JWT;

		if (jwtUtil.isExpired(token)) {
			log.info("JWT token expired");
			Cookie expiredCookie = new Cookie("JWT", null);
			expiredCookie.setMaxAge(0);
			expiredCookie.setPath("/");
			response.addCookie(expiredCookie);

			filterChain.doFilter(request, response);

			return;
		}

		String providerId = jwtUtil.getProviderId(token);
		String authorization = jwtUtil.getAuthorization(token);
		log.info("JWT authorization: {}", authorization);
		String name = jwtUtil.getName(token);
		String email = jwtUtil.getEmail(token);
		String profileImage = jwtUtil.getProfileImage(token);

		MemberResponse memberResponse = new MemberResponse(MemberAuthorization.AUTHORIZATION_USER.name(), name, email,
			providerId, profileImage);
		log.info("memberDto: {}", memberResponse);

		CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberResponse);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null,
			customOAuth2Member.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);

	}
}
