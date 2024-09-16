package org.badminton.domain.member.model.entity;

import org.badminton.domain.common.BaseTimeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
@Schema(description = "회원 엔티티")
public class MemberEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "회원 ID", example = "1")
	private Long memberId;

	@Schema(description = "oAuth 제공 ID", example = "1070449979547641023123")
	private String providerId;

	@Schema(description = "oAuth 로그인 이메일", example = "qosle@naver.com")
	private String email;

	@Schema(description = "회원 이름", example = "이선우")
	private String name;

	@Schema(description = "회원 역할", example = "ROLE_USER")
	private MemberRole role;

	public MemberEntity(String email, String name, String providerId, MemberRole role) {
		this.email = email;
		this.name = name;
		this.providerId = providerId;
		this.role = role;
	}
}
