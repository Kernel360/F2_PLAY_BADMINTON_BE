package org.badminton.domain.member.entity;

import java.time.LocalDateTime;

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

	private String authorization;

	private String profileImage;

	private boolean isDeleted = false;

	private LocalDateTime lastConnectionAt;

	private String refreshToken;

	public MemberEntity(String email, String name, String providerId, String profileImage,
		MemberAuthorization authorization) {
		this.email = email;
		this.name = name;
		this.providerId = providerId;
		this.authorization = authorization.name();
		this.profileImage = profileImage;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateMember(String profileImage) {
		this.profileImage = profileImage;
	}

	public void deleteMember() {
		this.isDeleted = true;
		this.updateLastConnection();
	}

	public void updateLastConnection() {
		this.lastConnectionAt = LocalDateTime.now();
	}

	public void reactivateMember() {
		this.isDeleted = false;
	}

	public boolean isMemberDeleted() {
		return this.isDeleted;
	}

}
