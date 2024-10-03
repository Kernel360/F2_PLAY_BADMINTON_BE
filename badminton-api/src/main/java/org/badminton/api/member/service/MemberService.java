package org.badminton.api.member.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.badminton.api.clubmember.model.dto.ClubMemberInfoResponse;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberDeleteResponse;
import org.badminton.api.member.model.dto.MemberDetailResponse;
import org.badminton.api.member.model.dto.MemberInfoResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.model.dto.MemberUpdateResponse;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.api.member.validator.MemberValidator;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.leaguerecord.repository.LeagueRecordRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final JwtUtil jwtUtil;
	private final MemberValidator memberValidator;
	private final MemberRepository memberRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final LeagueRecordRepository leagueRecordRepository;

	@Value("${spring.security.oauth2.revoke-url.naver}")
	private String naverRevokeUrl;

	@Value("${spring.security.oauth2.revoke-url.kakao}")
	private String kakaoRevokeUrl;

	@Value("${spring.security.oauth2.revoke-url.google}")
	private String googleRevokeUrl;

	@Value("${NAVER_CLIENT_ID}")
	private String naverClientId;

	@Value("${NAVER_CLIENT_SECRET}")
	private String naverClientSecret;

	public MemberDetailResponse getMemberInfo(Long memberId) {
		MemberEntity memberEntity = memberValidator.findMemberByMemberId(memberId);
		Optional<ClubMemberEntity> clubMemberEntity = clubMemberRepository.findByMember_MemberId(memberId);

		return clubMemberEntity
			.map(clubMember -> {
				LeagueRecordEntity leagueRecordEntity = leagueRecordRepository.findByClubMember(clubMember)
					.orElse(new LeagueRecordEntity(clubMember));

				return (MemberDetailResponse)ClubMemberInfoResponse.entityToClubMemberInfoResponse(memberEntity,
					clubMember, leagueRecordEntity);
			})
			.orElseGet(() -> (MemberDetailResponse)MemberInfoResponse.entityToMemberInfoResponse(memberEntity));
	}

	public MemberUpdateResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
		MemberEntity memberEntity = memberValidator.findMemberByMemberId(memberId);
		memberEntity.updateMember(memberUpdateRequest.profileImage());
		memberRepository.save(memberEntity);
		return MemberUpdateResponse.memberEntityToUpdateResponse(memberEntity);
	}

	public void logoutMember(Long memberId, HttpServletResponse response) {
		// unLinkOAuth(accessToken, null, null);
		MemberEntity member = memberValidator.findMemberByMemberId(memberId);
		member.updateRefreshToken(null);
		memberRepository.save(member);
		response.setHeader("Authorization", "");

		clearRefreshCookie(response);
		clearAccessCookie(response);

		log.info("Logged out member: {}", memberId);
	}

	public MemberDeleteResponse deleteMember(@AuthenticationPrincipal CustomOAuth2Member member,
		HttpServletRequest request,
		HttpServletResponse response) {

		// String accessToken = jwtUtil.extractAccessTokenFromHeader(request);
		// unLinkOAuth(accessToken, request, response);
		Long memberId = member.getMemberId();
		MemberDeleteResponse memberDeleteResponse = changeIsDeleted(memberId);

		clearRefreshCookie(response);
		clearAccessCookie(response);
		response.setHeader("Authorization", "");

		return memberDeleteResponse;

	}

	public void clearRefreshCookie(HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie("refresh_token", null);
		refreshTokenCookie.setMaxAge(0);
		refreshTokenCookie.setPath("/");
		response.addCookie(refreshTokenCookie);
	}

	public void clearAccessCookie(HttpServletResponse response) {
		Cookie accessTokenCookie = new Cookie("access_token", null);
		accessTokenCookie.setMaxAge(0);
		accessTokenCookie.setPath("/");
		response.addCookie(accessTokenCookie);
	}

	public MemberDeleteResponse changeIsDeleted(Long memberId) {
		MemberEntity memberEntity = memberValidator.findMemberByMemberId(memberId);
		memberEntity.deleteMember();
		memberRepository.save(memberEntity);
		log.info("Member marked as deleted: {}", memberId);
		return MemberDeleteResponse.memberEntityToDeleteResponse(memberEntity);
	}

	private String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtUtil.extractRefreshTokenFromCookie(request);
		if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
			String memberId = jwtUtil.getMemberId(refreshToken);
			List<String> roles = jwtUtil.getRoles(refreshToken);
			String registrationId = jwtUtil.getRegistrationId(refreshToken);

			String newAccessToken = jwtUtil.createAccessToken(memberId, roles, registrationId);
			jwtUtil.setAccessTokenHeader(response, newAccessToken);
			return newAccessToken;
		}
		return null;
	}

	public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String newAccessToken = refreshAccessToken(request, response);
		if (newAccessToken != null) {
			return ResponseEntity.ok(newAccessToken);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}
	}

	// private void unLinkOAuth(String accessToken, HttpServletRequest request, HttpServletResponse response) {
	// 	String memberId = jwtUtil.getMemberId(accessToken);
	// 	String registrationId = jwtUtil.getRegistrationId(accessToken);
	//
	// 	if (oAuthAccessToken == null) {
	// 		log.warn("OAuth Access Token is null for member: {}", memberId);
	// 		return;
	// 	}
	//
	// 	// 액세스 토큰 유효성 검사 및 갱신
	// 	if (jwtUtil.isTokenExpired(accessToken) && request != null && response != null) {
	// 		accessToken = refreshAccessToken(request, response);
	// 		if (accessToken == null) {
	// 			log.error("Failed to refresh access token for member: {}", memberId);
	// 			return;
	// 		}
	// 		oAuthAccessToken = jwtUtil.getOAuthToken(accessToken);
	// 	}
	//
	// 	String unlinkUrl = getUnlinkUrl(registrationId, oAuthAccessToken);
	// 	boolean useAuthHeader = "naver".equals(registrationId) || "kakao".equals(registrationId);
	// 	String method = "google".equals(registrationId) ? "GET" : "POST";
	//
	// 	unlinkAccount(unlinkUrl, method, oAuthAccessToken, useAuthHeader);
	// 	removeOAuthDataFromUser(memberId, registrationId);
	// }

	public void unlinkAccount(String revokeUrl, String method, String accessToken, boolean useAuthHeader) {
		try {
			URL url = new URL(revokeUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);

			if (useAuthHeader && accessToken != null) {
				connection.setRequestProperty("Authorization", "Bearer " + accessToken);
			}

			connection.setRequestProperty("Content-Length", "0");

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				log.info("Account successfully unlinked");
			} else {
				log.error("Failed to unlink account, response code: {}", responseCode);
			}
		} catch (IOException e) {
			log.error("Error occurred while unlinking account", e);
		}
	}

	// private String getUnlinkUrl(String registrationId, String oAuthAccessToken) {
	// 	switch (registrationId) {
	// 		case "google":
	// 			return googleRevokeUrl + "?token=" + oAuthAccessToken;
	// 		case "naver":
	// 			return naverRevokeUrl + "?grant_type=delete&client_id=" + naverClientId
	// 				+ "&client_secret=" + naverClientSecret + "&access_token=" + oAuthAccessToken;
	// 		case "kakao":
	// 			return kakaoRevokeUrl;
	// 		default:
	// 			throw new IllegalArgumentException("Unsupported OAuth provider: " + registrationId);
	// 	}
	// }
	//
	// private void removeOAuthDataFromUser(String memberId, String registrationId) {
	// 	MemberEntity member = memberValidator.findMemberByMemberId(Long.valueOf(memberId));
	// 	// Implement logic to remove OAuth-specific data
	// 	member.updateRefreshToken(null);
	// 	// Add more fields to clear if necessary
	// 	memberRepository.save(member);
	// 	log.info("Removed OAuth data for {} from user {}", registrationId, memberId);
	// }

}