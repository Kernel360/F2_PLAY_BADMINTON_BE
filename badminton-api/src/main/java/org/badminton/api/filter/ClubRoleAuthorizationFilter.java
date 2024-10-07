package org.badminton.api.filter;

import java.io.IOException;

import org.badminton.api.config.security.ClubPermissionEvaluator;
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
public class ClubRoleAuthorizationFilter extends OncePerRequestFilter {

	private final ClubPermissionEvaluator clubPermissionEvaluator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String clubId = SecurityUtil.extractClubIdFromRequest(request);
		String method = request.getMethod();
		String path = request.getRequestURI();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomOAuth2Member) {
			if (path.matches("/v1/club/\\d+/league") && "POST".equalsIgnoreCase(method)) {
				if (!clubPermissionEvaluator.hasClubRole(auth, Long.parseLong(clubId), "OWNER", "MANAGER")) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient club role for creating league");
					return;
				}
			}
			// Add more conditions for other endpoints as needed
		}

		filterChain.doFilter(request, response);
	}
}
