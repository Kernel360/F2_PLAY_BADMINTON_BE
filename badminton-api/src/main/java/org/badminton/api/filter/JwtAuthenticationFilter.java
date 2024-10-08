package org.badminton.api.filter;

import java.io.IOException;
import java.util.List;

import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.jwt.JwtUtil;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ClubMemberService clubMemberService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = jwtUtil.extractAccessTokenFromCookie(request);

		if (token != null && jwtUtil.validateToken(token)) {
			String memberId = jwtUtil.getMemberId(token);
			List<ClubMemberEntity> clubMemberEntities = clubMemberService.findAllClubMembersByMemberId(
				Long.valueOf(memberId));

			String oAuthAccessToken = jwtUtil.getOAuthToken(token);

			MemberResponse memberResponse = new MemberResponse(Long.valueOf(memberId),
				MemberAuthorization.AUTHORIZATION_USER.toString());
			CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberResponse,
				jwtUtil.getRegistrationId(token), oAuthAccessToken);

			for (ClubMemberEntity clubMember : clubMemberEntities) {
				customOAuth2Member.addClubRole(clubMember.getClub().getClubId(), clubMember.getRole().name());
			}

			Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2Member, null,
				customOAuth2Member.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/") || path.equals("/groups") || path.startsWith("/oauth2") || path.startsWith("/login")
			|| path.startsWith("/api") || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
			;
	}

}
