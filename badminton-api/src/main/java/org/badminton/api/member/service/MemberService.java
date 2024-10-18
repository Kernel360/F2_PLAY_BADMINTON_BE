package org.badminton.api.member.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberDeleteResponse;
import org.badminton.api.member.model.dto.MemberIsClubMemberResponse;
import org.badminton.api.member.model.dto.MemberMyPageResponse;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.oauth2.dto.CustomOAuth2Member;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.leaguerecord.repository.LeagueRecordRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final JwtUtil jwtUtil;
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

	@Value("${custom.server.domain}")
	private String domain;

	@Value("${spring.jwt.secret}")
	private String jwtSecret;

	public MemberIsClubMemberResponse getMemberIsClubMember(Long memberId) {
		boolean isClubMember = clubMemberRepository.existsByMember_MemberIdAndDeletedFalse(memberId);
		if (isClubMember) {
			ClubMemberEntity clubMemberEntity = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
				.get();
			ClubMemberRole clubMemberRole = clubMemberEntity.getRole();
			Long clubId = clubMemberEntity.getClub().getClubId();

			return new MemberIsClubMemberResponse(true, clubMemberRole, clubId);
		}
		return new MemberIsClubMemberResponse(false, null, null);
	}

	public MemberMyPageResponse getMemberInfo(Long memberId) {
		MemberEntity memberEntity = findMemberByMemberId(memberId);
		ClubMemberEntity clubMemberEntity = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
			.orElse(null);
		LeagueRecordEntity leagueRecordEntity = null;

		if (clubMemberEntity != null) {
			leagueRecordEntity = leagueRecordRepository.findByClubMember(clubMemberEntity)
				.orElse(new LeagueRecordEntity(clubMemberEntity));
			return MemberMyPageResponse.fromMemberEntityAndClubMemberEntity(memberEntity, clubMemberEntity,
				leagueRecordEntity);
		}
		return MemberMyPageResponse.fromMemberEntity(memberEntity);
	}

	@Transactional
	public MemberResponse updateProfileImage(Long memberId, String imageUrl) {
		MemberEntity memberEntity = findMemberByMemberId(memberId);
		memberEntity.updateMember(imageUrl);
		memberRepository.save(memberEntity);
		return MemberResponse.memberEntityToResponse(memberEntity);
	}

	public void logoutMember(Long memberId, HttpServletResponse response) {
		MemberEntity member = findMemberByMemberId(memberId);
		member.updateRefreshToken(null);
		memberRepository.save(member);
		response.setHeader("Authorization", "");

		clearRefreshCookie(response);
		clearAccessCookie(response);

		log.info("Logged out member: {}", memberId);
	}

	// TODO: http request service 까지 오면 안됨
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
		ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.domain(domain)
			.path("/")
			.maxAge(0)  // 만료 시간을 0으로 설정하여 쿠키 삭제
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void clearAccessCookie(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("access_token", "")
			.httpOnly(true)
			.secure(true)
			.sameSite("None")
			.domain(domain)
			.path("/")
			.maxAge(0)  // 만료 시간을 0으로 설정하여 쿠키 삭제
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	public MemberDeleteResponse changeIsDeleted(Long memberId) {
		MemberEntity memberEntity = findMemberByMemberId(memberId);
		memberEntity.doWithdrawal();
		memberRepository.save(memberEntity);
		log.info("Member marked as deleted: {}", memberId);
		return MemberDeleteResponse.memberEntityToDeleteResponse(memberEntity);
	}

	public String refreshCustomAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String customAccessToken = jwtUtil.extractCustomAccessTokenFromCookie(request);


		try {
			Claims claims = Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
				.build()
				.parseSignedClaims(customAccessToken)
				.getPayload();

			String memberId = claims.get("memberId", String.class);

			MemberEntity memberEntity = memberRepository.findByMemberId(Long.valueOf(memberId)).get();

			ClubMemberEntity clubMemberEntity = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(
				Long.valueOf(memberId)).orElse(null);

			Set<String> roles = new HashSet<>();

			if (clubMemberEntity != null) {
				roles.add(clubMemberEntity.getClub().getClubId() + ":ROLE_" + clubMemberEntity.getRole().name());
			}
			roles.add("ROLE_" + memberEntity.getAuthorization());


			List<String> roleList = new ArrayList<>(roles);

			return jwtUtil.createCustomAccessToken(memberId, roleList);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
	}

	public void unLinkOAuth(String registrationId, String accessToken, HttpServletRequest request,
		HttpServletResponse response) {
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
		String unlinkUrl = UriComponentsBuilder.fromHttpUrl(googleRevokeUrl)
			.queryParam("token", accessToken)
			.build()
			.toUriString();

		ResponseEntity<String> response = restTemplate.getForEntity(unlinkUrl, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("Successfully unlinked Google account");
		} else {
			log.error("Failed to unlink Google account. Status: {}, Body: {}",
				response.getStatusCode(), response.getBody());
		}
	}

	private void unlinkNaver(String accessToken) {
		String unlinkUrl = UriComponentsBuilder.fromHttpUrl(naverRevokeUrl)
			.queryParam("grant_type", "delete")
			.queryParam("client_id", naverClientId)
			.queryParam("client_secret", naverClientSecret)
			.queryParam("access_token", accessToken)
			.queryParam("service_provider", "NAVER")
			.build()
			.toUriString();

		log.info("Sending request to unlink Naver account: {}", unlinkUrl);

		ResponseEntity<String> response = restTemplate.getForEntity(unlinkUrl, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("Successfully unlinked Naver account");
		} else {
			log.error("Failed to unlink Naver account. Status: {}, Body: {}",
				response.getStatusCode(), response.getBody());
		}

	}

	private void unlinkKakao(String accessToken) {
		String unlinkUrl = UriComponentsBuilder.fromHttpUrl(kakaoRevokeUrl)
			.build()
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>("", headers);

		ResponseEntity<String> response = restTemplate.exchange(unlinkUrl, HttpMethod.POST, entity, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("Successfully unlinked Kakao account");
		} else {
			log.error("Failed to unlink Kakao account. Status: {}, Body: {}",
				response.getStatusCode(), response.getBody());
		}
	}

	private MemberEntity findMemberByMemberId(Long memberId) {
		return memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new MemberNotExistException(memberId));
	}
}
