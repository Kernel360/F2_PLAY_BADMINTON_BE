package org.badminton.domain.clubmember.entity;

import lombok.Getter;

@Getter
public enum ClubMemberRole {
	ROLE_OWNER("owner"),
	ROLE_MANAGER("manager"),
	ROLE_USER("user");

	private final String description;

	ClubMemberRole(String description) {
		this.description = description;
	}

}
