package org.badminton.api.member.jwt;

import java.io.IOException;
import java.util.Optional;

import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.member.entity.MemberAuthorization;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ClubMemberService clubMemberService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/") || path.equals("/groups") || path.startsWith("/oauth2") || path.startsWith("/login")
			|| path.startsWith("/api") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = jwtUtil.extractAccessTokenFromCookie(request);

		if (token == null) {
			log.info("No token found");
			filterChain.doFilter(request, response);
			return;
		}

		if (!jwtUtil.validateToken(token)) {
			log.info("Invalid token");
			filterChain.doFilter(request, response);
			return;
		}

		String memberId = jwtUtil.getMemberId(token);

		Optional<ClubMemberEntity> clubMemberEntity = clubMemberService.findClubMemberByMemberId(memberId);

		MemberResponse memberResponse = new MemberResponse(Long.valueOf(memberId),
			MemberAuthorization.AUTHORIZATION_USER.toString());
		CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberResponse, null);
		if (clubMemberEntity.isPresent()) {
			String clubMemberRole = clubMemberEntity.get().getRole().name();
			log.info("Club member role: {}", clubMemberRole);
			customOAuth2Member.updateClubRole(clubMemberRole);
		}

		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null,
			customOAuth2Member.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		log.info("현재 사용자의 ROLE: {}", authToken.getAuthorities());
		filterChain.doFilter(request, response);

	}
}
