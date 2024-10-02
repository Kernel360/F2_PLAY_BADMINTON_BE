package org.badminton.api.match.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoublesMatchResponse {
	private TeamResponse team1;
	private TeamResponse team2;
}
