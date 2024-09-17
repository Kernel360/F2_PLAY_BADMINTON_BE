package org.badminton.domain.member.model.entity;

import org.badminton.domain.common.BaseTimeEntity;

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
public class MemberEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	private String providerId;

	private String email;

	private String name;

	private MemberRole role;

	public MemberEntity(String email, String name, String providerId, MemberRole role) {
		this.email = email;
		this.name = name;
		this.providerId = providerId;
		this.role = role;
	}
}
