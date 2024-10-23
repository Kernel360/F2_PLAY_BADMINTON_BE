package org.badminton.api.interfaces.member.dto;

import org.badminton.domain.domain.member.entity.MemberEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "회원 요청 DTO")
public class MemberResponse {

    @Schema(description = "회원 id", example = "1")
    private Long memberId;

    @Schema(description = "회원 역할", example = "AUTHORIZATION_USER")
    private String authorization;

    @Schema(description = "회원 이름", example = "이선우")
    private String name;

    @Schema(description = "oAuth 로그인 이메일", example = "qosle@naver.com")
    private String email;

    @Schema(description = "oAuth 제공 ID", example = "1070449979547641023123")
    private String providerId;

    @Schema(description = "oAuth 제공 이미지", example = "1070449979547641023123")
    private String profileImage;

    public static MemberResponse memberEntityToResponse(MemberEntity memberEntity) {
        return new MemberResponse(memberEntity.getMemberId(), memberEntity.getAuthorization(), memberEntity.getName(),
                memberEntity.getEmail(),
                memberEntity.getProviderId(), memberEntity.getProfileImage());
    }

    public MemberResponse(
            Long memberId, String authorization) {
        this.memberId = memberId;
        this.authorization = authorization;
    }

}


