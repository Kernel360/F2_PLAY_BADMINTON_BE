package org.badminton.api.interfaces.member.dto;

import org.badminton.domain.domain.member.entity.MemberEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 로그아웃 responseDto")
public record MemberLogoutResponse(
        @Schema(description = "회원 역할", example = "AUTHORIZATION_USER")
        String authorization,

        @Schema(description = "회원 이름", example = "김철수")
        String name,

        @Schema(description = "oAuth 로그인 이메일", example = "qosle@naver.com")
        String email,

        @Schema(description = "oAuth 제공 ID", example = "1070449979547641023123")
        String providerId,

        @Schema(description = "oAuth 제공 이미지", example = "1070449979547641023123")
        String profileImage
) {
    public static MemberLogoutResponse memberEntityToLogoutResponse(MemberEntity memberEntity) {
        return new MemberLogoutResponse(memberEntity.getAuthorization(), memberEntity.getName(),
                memberEntity.getEmail(),
                memberEntity.getProviderId(), memberEntity.getProfileImage());
    }
}
