package org.badminton.api.member.jwt;

import java.io.IOException;

import org.badminton.api.member.model.dto.CustomOAuth2Member;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.domain.member.entity.MemberRole;
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
			|| path.startsWith("/api") || path.startsWith("/swagger-ui") || path.startsWith("/api-docs");

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorization = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			log.info("cookie: {}", cookie.getName());
			if (cookie.getName().equals("Authorization")) {
				authorization = cookie.getValue();
			}
		}

		if (authorization == null) {
			log.info("Authorization cookie not found");
			filterChain.doFilter(request, response);

			return;
		}

		String token = authorization;

		if (jwtUtil.isExpired(token)) {
			log.info("JWT token expired");
			filterChain.doFilter(request, response);

			return;
		}

		String providerId = jwtUtil.getProviderId(token);
		String role = jwtUtil.getRole(token);
		log.info("JWT role: {}", role);
		String name = jwtUtil.getName(token);
		String email = jwtUtil.getEmail(token);

		MemberResponse memberResponse = new MemberResponse(MemberRole.fromDescription(role), name, email, providerId);
		log.info("memberDto: {}", memberResponse);

		CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberResponse);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null,
			customOAuth2Member.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);

	}
}
