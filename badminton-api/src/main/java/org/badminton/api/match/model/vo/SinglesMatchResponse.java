package org.badminton.api.match.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SinglesMatchResponse {
	private Long participant1Id;
	private Long participant2Id;
}
