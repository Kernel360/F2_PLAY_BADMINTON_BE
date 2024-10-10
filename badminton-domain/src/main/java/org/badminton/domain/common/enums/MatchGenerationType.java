package org.badminton.domain.common.enums;

public enum MatchGenerationType {
	RANDOM("무작위"),
	TIER("티어");

	private final String description;

	MatchGenerationType(String description) {
		this.description = description;
	}

	public String getDescription() {
        return description;
    }

}
