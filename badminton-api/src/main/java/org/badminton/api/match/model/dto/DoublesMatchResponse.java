package org.badminton.api.match.model.dto;

public record DoublesMatchResponse(
	TeamResponse team1,
	TeamResponse team2
) {
}
