package org.badminton.domain.member.model.entity;

public enum MemberRole {
	ROLE_ADMIN("admin"),
	ROLE_USER("user");

	private String description;

	MemberRole(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
