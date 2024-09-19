package org.badminton.api.member.service;

import static org.badminton.api.member.model.dto.MemberRequest.*;
import static org.badminton.api.member.model.dto.MemberResponse.*;

import org.badminton.api.member.model.dto.CustomOAuth2Member;
import org.badminton.api.member.model.dto.GoogleResponse;
import org.badminton.api.member.model.dto.KakaoResponse;
import org.badminton.api.member.model.dto.MemberRequest;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.model.dto.NaverResponse;
import org.badminton.api.member.model.dto.OAuthResponse;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.entity.MemberRole;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2MemberService is being executed");
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("USer details: {}", oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuthResponse oAuth2Response = null;
		switch (registrationId) {
			case "naver" -> oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
			case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
			case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
			default -> {
				return null;
			}
		}
		// String providerId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		String providerId = oAuth2Response.getProviderId();
		MemberEntity existData = memberRepository.findByProviderId(providerId);

		if (existData == null) {

			MemberRequest memberRequest = new MemberRequest(MemberRole.ROLE_USER, oAuth2Response.getName(),
				oAuth2Response.getEmail(), providerId);

			MemberEntity memberEntity = memberRequestToEntity(memberRequest);

			memberRepository.save(memberEntity);

			MemberResponse memberResponse = memberEntityToResponse(memberEntity);

			return new CustomOAuth2Member(memberResponse);
		} else {

			memberRepository.save(existData);

			MemberResponse memberResponse = memberEntityToResponse(existData);

			return new CustomOAuth2Member(memberResponse);
		}
	}
}
