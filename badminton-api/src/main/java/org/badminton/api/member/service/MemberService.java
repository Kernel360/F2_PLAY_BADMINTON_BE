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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	private final RestTemplate restTemplate;

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

	//TODO: 제네릭 사용해보기

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

		String accessToken = member.getOAuthAccessToken();
		String registrationId = member.getRegistrationId();
		unLinkOAuth(registrationId, accessToken, request, response);
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
			String oAuthAccessToken = jwtUtil.getOAuthToken(refreshToken);

			String newAccessToken = jwtUtil.createAccessToken(memberId, roles, registrationId, oAuthAccessToken);
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

	public void unLinkOAuth(String registrationId, String accessToken, HttpServletRequest request, HttpServletResponse response) {
		log.info("Unlinking OAuth for provider: {}", registrationId);

		try {
			switch (registrationId.toLowerCase()) {
				case "google":
					unlinkGoogle(accessToken);
					break;
				case "naver":
					unlinkNaver(accessToken);
					break;
				case "kakao":
					unlinkKakao(accessToken);
					break;
				default:
					log.error("Unsupported OAuth provider: {}", registrationId);
					throw new IllegalArgumentException("Unsupported OAuth provider");
			}
			log.info("Successfully unlinked OAuth for provider: {}", registrationId);
		} catch (Exception e) {
			log.error("Error unlinking OAuth for provider: {}", registrationId, e);
		}
	}

	private void unlinkGoogle(String accessToken) {
		String unlinkUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
		restTemplate.getForObject(unlinkUrl, String.class);
	}

	private void unlinkNaver(String accessToken) {
		String unlinkUrl = "https://nid.naver.com/oauth2.0/token" +
			"?grant_type=delete" +
			"&client_id=" + naverClientId +
			"&client_secret=" + naverClientSecret +
			"&access_token=" + accessToken +
			"&service_provider=NAVER";
		restTemplate.getForObject(unlinkUrl, String.class);
	}

	private void unlinkKakao(String accessToken) {
		String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		restTemplate.postForObject(unlinkUrl, entity, String.class);
	}

}