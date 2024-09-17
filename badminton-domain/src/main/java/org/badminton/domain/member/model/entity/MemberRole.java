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

	public static MemberRole fromDescription(String description) {
		for (MemberRole memberRole : MemberRole.values()) {
			if (memberRole.description.equals(description)) {
				return memberRole;
			}
		}
		throw new IllegalArgumentException(description);
	}

}
