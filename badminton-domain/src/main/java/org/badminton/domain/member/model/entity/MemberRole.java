package org.badminton.domain.member.model.entity;

import lombok.Getter;

@Getter
public enum MemberRole {
	ROLE_ADMIN("admin"),
	ROLE_USER("user");

	private final String description;

	MemberRole(String description) {
		this.description = description;
	}

}
