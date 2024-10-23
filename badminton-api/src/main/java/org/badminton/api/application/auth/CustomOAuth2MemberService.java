package org.badminton.api.application.auth;

import static org.badminton.api.interfaces.member.dto.MemberResponse.memberEntityToResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badminton.api.interfaces.member.dto.MemberRequest;
import org.badminton.api.interfaces.member.dto.MemberResponse;
import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
import org.badminton.api.interfaces.oauth.dto.GoogleResponse;
import org.badminton.api.interfaces.oauth.dto.KakaoResponse;
import org.badminton.api.interfaces.oauth.dto.NaverResponse;
import org.badminton.api.interfaces.oauth.dto.OAuthResponse;
import org.badminton.domain.domain.member.entity.MemberAuthorization;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.badminton.domain.infrastructures.member.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        log.info("registrationId: {}", registrationId);
        OAuthResponse oAuth2Response = null;
        String oAuthAccessToken = userRequest.getAccessToken().getTokenValue();

        switch (registrationId) {
            case "naver" -> oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            default -> {
                return null;
            }
        }
        String providerId = oAuth2Response.getProviderId();
        MemberEntity memberEntity = memberRepository.findByProviderId(providerId).orElse(null);

        if (memberEntity == null) {

            MemberRequest memberRequest = new MemberRequest(MemberAuthorization.AUTHORIZATION_USER,
                    oAuth2Response.getName(),
                    oAuth2Response.getEmail(), providerId, oAuth2Response.getProfileImage());

            memberEntity = memberRequest.memberRequestToEntity();

        } else if (memberEntity.isDeleted()) {
            memberEntity.reactivateMember();
        }

        memberRepository.save(memberEntity);

        MemberResponse memberResponse = memberEntityToResponse(memberEntity);
        return new CustomOAuth2Member(memberResponse, registrationId, oAuthAccessToken);

    }
}
