package org.badminton.api.member.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
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

	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;

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

	public void updateMember(HttpServletRequest request, MemberUpdateRequest memberUpdateRequest) {
		String providerId = jwtUtil.extractProviderIdFromRequest(request);
		if (providerId == null) {
			throw new IllegalArgumentException("jwt provider id is null");
		}
		MemberEntity memberEntity = memberRepository.findByProviderId(providerId);
		if (memberEntity == null) {
			throw new IllegalArgumentException("Member not found");
		}

		memberEntity.updateMember(memberUpdateRequest.getProfileImage());
		memberRepository.save(memberEntity);
	}

	public void logoutMember(HttpServletRequest request, HttpServletResponse response) {
		unLinkOAuth(request);
		removeJwtCookie(response);
	}

	public void deleteMember(HttpServletRequest request, HttpServletResponse response) {
		String jwtToken = unLinkOAuth(request);
		changeIsDeleted(jwtUtil.getProviderId(jwtToken));
		removeJwtCookie(response);
	}

	private void removeJwtCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("JWT", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	private String unLinkOAuth(HttpServletRequest request) {
		String jwtToken = jwtUtil.extractJwtTokenFromRequest(request);
		if (jwtToken == null) {
			throw new IllegalArgumentException("Invalid JWT token");
		}
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

	public void changeIsDeleted(String providerId) {
		MemberEntity memberEntity = memberRepository.findByProviderId(providerId);

		memberEntity.deleteMember();
		memberRepository.save(memberEntity);
		log.info("Member marked as deleted: {}", providerId);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void deleteMembersFromDb() {
		log.info("Deleting members");

		List<MemberEntity> deleteMembers = memberRepository.findAllByIsDeletedTrue();

		for (MemberEntity memberEntity : deleteMembers) {
			LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
			if (lastConnectionAt != null) {
				if (ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7) {
					memberRepository.delete(memberEntity);
					log.info("Delete member: {}", memberEntity.getProviderId());
				}
			}
		}
		log.info("Deleting members");
	}

	public void kakaoUnlink(String accessToken) {
		String formattedUrl = kakaoRevokeUrl;
		unlinkAccount(formattedUrl, "POST", accessToken, true);
	}

	public void googleUnlink(String accessToken) {
		String formattedUrl = googleRevokeUrl.replace("{access_token}", accessToken);
		unlinkAccount(formattedUrl, "GET", null, false);
	}

	public void naverUnlink(String accessToken) {
		String formattedUrl = naverRevokeUrl
			.replace("{client_id}", naverClientId)
			.replace("{client_secret}", naverClientSecret)
			.replace("{access_token}", accessToken);

		unlinkAccount(formattedUrl, "GET", null, false);
	}

	private void unlinkAccount(String revokeUrl, String method, String accessToken, boolean useAuthHeader) {
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

}
