package org.badminton.api.member.validator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberValidator {

	//TODO: validator 에서 repository 의존 끊기
	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;

	public void saveMember(MemberEntity memberEntity) {
		memberRepository.save(memberEntity);
	}

	public List<MemberEntity> provideMemberListByIsDeletedTrue() {
		return memberRepository.findAllByIsDeletedTrue();
	}

	public void deleteMember(MemberEntity memberEntity) {
		memberRepository.delete(memberEntity);
	}

	public MemberEntity findMemberByProviderId(String providerId) {
		return memberRepository.findByProviderId(providerId).orElseThrow(() ->
			new MemberNotExistException(ErrorCode.MEMBER_NOT_EXIST, providerId));
	}

	public String extractJwtToken(HttpServletRequest request) {
		return jwtUtil.extractJwtTokenFromRequest(request);
	}

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

}

