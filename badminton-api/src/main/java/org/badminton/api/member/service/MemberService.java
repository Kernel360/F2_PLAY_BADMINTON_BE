package org.badminton.api.member.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberDeleteResponse;
import org.badminton.api.member.model.dto.MemberLogoutResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.api.member.model.dto.MemberUpdateResponse;
import org.badminton.api.member.validator.MemberValidator;
import org.badminton.domain.member.entity.MemberEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

	public MemberUpdateResponse updateMember(HttpServletRequest request, MemberUpdateRequest memberUpdateRequest) {
		String providerId = jwtUtil.extractProviderIdFromRequest(request);
		MemberEntity memberEntity = memberValidator.findMemberByProviderId(providerId);
		memberEntity.updateMember(memberUpdateRequest.profileImage());
		memberValidator.saveMember(memberEntity);
		return MemberUpdateResponse.memberEntityToUpdateResponse(memberEntity);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void deleteMembersFromDb() {
		log.info("Deleting members");

		List<MemberEntity> deleteMembers = memberValidator.provideMemberListByIsDeletedTrue();

		for (MemberEntity memberEntity : deleteMembers) {
			LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
			if (lastConnectionAt != null) {
				if (ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7) {
					memberValidator.deleteMember(memberEntity);
					log.info("Delete member: {}", memberEntity.getProviderId());
				}
			}
		}
		log.info("Deleting members");
	}

	public MemberDeleteResponse deleteMember(HttpServletRequest request, HttpServletResponse response) {
		String jwtToken = unLinkOAuth(request);
		MemberDeleteResponse memberDeleteResponse = changeIsDeleted(jwtUtil.getProviderId(jwtToken));
		removeJwtCookie(response);
		return memberDeleteResponse;
	}

	public MemberDeleteResponse changeIsDeleted(String providerId) {
		MemberEntity memberEntity = memberValidator.findMemberByProviderId(providerId);
		memberEntity.deleteMember();
		memberValidator.saveMember(memberEntity);
		log.info("Member marked as deleted: {}", providerId);
		return MemberDeleteResponse.memberEntityToDeleteResponse(memberEntity);
	}

	public MemberLogoutResponse logoutMember(HttpServletRequest request, HttpServletResponse response) {
		String jwtToken = unLinkOAuth(request);
		removeJwtCookie(response);
		String providerId = jwtUtil.getProviderId(jwtToken);
		MemberEntity memberEntity = memberValidator.findMemberByProviderId(providerId);
		return MemberLogoutResponse.memberEntityToLogoutResponse(memberEntity);
	}

	private void removeJwtCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("JWT", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	private String unLinkOAuth(HttpServletRequest request) {
		String jwtToken = memberValidator.extractJwtToken(request);
		String accessToken = jwtUtil.getAccessToken(jwtToken);
		String registrationId = jwtUtil.getRegistrationId(jwtToken);

		// 연결 끊기 로직 (Google, Naver, Kakao)
		if ("google".equals(registrationId)) {
			googleUnlink(accessToken);
		} else if ("naver".equals(registrationId)) {
			naverUnlink(accessToken);
		} else if ("kakao".equals(registrationId)) {
			kakaoUnlink(accessToken);
		}
		return jwtToken;
	}

	public void kakaoUnlink(String accessToken) {
		String formattedUrl = kakaoRevokeUrl;
		memberValidator.unlinkAccount(formattedUrl, "POST", accessToken, true);
	}

	public void googleUnlink(String accessToken) {
		String formattedUrl = googleRevokeUrl.replace("{access_token}", accessToken);
		memberValidator.unlinkAccount(formattedUrl, "GET", null, false);
	}

	public void naverUnlink(String accessToken) {
		String formattedUrl = naverRevokeUrl
			.replace("{client_id}", naverClientId)
			.replace("{client_secret}", naverClientSecret)
			.replace("{access_token}", accessToken);

		memberValidator.unlinkAccount(formattedUrl, "GET", null, false);
	}

}
