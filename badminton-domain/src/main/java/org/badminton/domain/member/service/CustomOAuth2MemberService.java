package org.badminton.domain.member.service;

import org.badminton.domain.member.model.dto.CustomOAuth2Member;
import org.badminton.domain.member.model.dto.GoogleResponse;
import org.badminton.domain.member.model.dto.KakaoResponse;
import org.badminton.domain.member.model.dto.MemberDto;
import org.badminton.domain.member.model.dto.NaverResponse;
import org.badminton.domain.member.model.dto.OAuthResponse;
import org.badminton.domain.member.model.entity.MemberEntity;
import org.badminton.domain.member.model.entity.MemberRole;
import org.badminton.domain.member.model.mapper.MemberMapper;
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
	private final MemberMapper memberMapper;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2MemberService is being executed");

		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("USer details: {}", oAuth2User);
		System.out.println("CustomOAuth2UserService :super.loadUser(userRequest) " + oAuth2User);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuthResponse oAuth2Response = null;
		if (registrationId.equals("naver")) {

			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("google")) {

			oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
		} else if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {

			return null;
		}
		// String providerId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		String providerId = oAuth2Response.getProviderId();
		MemberEntity existData = memberRepository.findByProviderId(providerId);

		if (existData == null) {

			MemberDto memberDto = new MemberDto(MemberRole.ROLE_USER, oAuth2Response.getName(),
				oAuth2Response.getEmail(),
				providerId);

			MemberEntity memberEntity = memberMapper.toEntity(memberDto);

			memberRepository.save(memberEntity);

			return new CustomOAuth2Member(memberDto);
		} else {

			memberRepository.save(existData);

			MemberDto memberDto = memberMapper.toDto(existData);

			return new CustomOAuth2Member(memberDto);
		}
	}
}
