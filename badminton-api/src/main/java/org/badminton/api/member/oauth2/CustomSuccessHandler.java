package org.badminton.api.member.oauth2;

import java.io.IOException;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
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
	private final ClubMemberRepository clubMemberRepository;

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

		ClubMemberEntity clubMemberEntity = clubMemberRepository.findByMember_MemberId(memberId)
			.orElse(null);
		if (clubMemberEntity != null) {
			String clubRole = clubMemberEntity.getRole().name();
			customUserDetails.updateClubRole(clubRole);
		}

		String roles = String.join(",", customUserDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority).toList());

		String accessToken = jwtUtil.createAccessToken(String.valueOf(memberId), roles, registrationId);

		String refreshToken = jwtUtil.createRefreshToken(String.valueOf(memberId), roles, registrationId);

		memberEntity.updateRefreshToken(refreshToken);
		memberRepository.save(memberEntity);

		clearSession(request, response);

		jwtUtil.setAccessTokenHeader(response, accessToken);
		jwtUtil.setAccessTokenCookie(response, accessToken);
		jwtUtil.setRefreshTokenCookie(response, refreshToken);

		response.sendRedirect("http://localhost:3000");

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


