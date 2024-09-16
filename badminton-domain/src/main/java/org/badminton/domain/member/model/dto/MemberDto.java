package org.badminton.domain.member.model.dto;

import org.badminton.domain.member.model.entity.MemberRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDto {

	@Schema(description = "회원 역할", example = "ROLE_USER")
	private MemberRole role;

	@Schema(description = "회원 이름", example = "이선우")
	private String name;

	@Schema(description = "oAuth 로그인 이메일", example = "qosle@naver.com")
	private String email;

	@Schema(description = "oAuth 제공 ID", example = "1070449979547641023123")
	private String providerId;

}


