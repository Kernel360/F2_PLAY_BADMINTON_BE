package org.badminton.api.match.model.dto;

public record SinglesMatchResponse(
	String participant1Name,
	String participant1Image,
	String participant2Name,
	String participant2Image
) {

}
