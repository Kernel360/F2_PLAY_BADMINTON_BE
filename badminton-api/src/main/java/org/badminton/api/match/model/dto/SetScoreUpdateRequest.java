package org.badminton.api.match.model.dto;

public record SetScoreUpdateRequest(
	int score1,
	int score2
) {
}
