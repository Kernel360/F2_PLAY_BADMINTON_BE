package org.badminton.api.match.model.vo;

public record DoublesMatchResponse(
	TeamResponse team1,
	TeamResponse team2
) {
}
