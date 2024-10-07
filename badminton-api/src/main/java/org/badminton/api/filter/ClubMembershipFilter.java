package org.badminton.api.filter;

import java.io.IOException;

import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.config.security.SecurityUtil;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
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
public class ClubMembershipFilter extends OncePerRequestFilter {

	private final ClubMemberService clubMemberService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return !path.startsWith("/v1/club") || SecurityUtil.isPublicPath(request) || path.startsWith("/v1/member");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String clubId = SecurityUtil.extractClubIdFromRequest(request);

		if (clubId != null && !isClubMember(clubId)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not a member of this club");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isClubMember(String clubId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof CustomOAuth2Member)) {
			return false;
		}

		CustomOAuth2Member member = (CustomOAuth2Member)auth.getPrincipal();
		return clubMemberService.isMemberOfClub(member.getMemberId(), Long.valueOf(clubId));
	}
}


