package org.badminton.api.match.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.badminton.domain.match.model.entity.DoublesSetEntity;

public record DoublesSetResponse(
	int setIndex,
	int score1,
	int score2
) {

	public static List<DoublesSetResponse> entityToDoublesSetResponse(List<DoublesSetEntity> doublesSets) {
		List<DoublesSetResponse> doublesSetResponseList = new ArrayList<>();
		for (DoublesSetEntity doublesSet : doublesSets) {
			doublesSetResponseList.add(new DoublesSetResponse(doublesSet.getSetIndex(), doublesSet.getTeam1Score(),
				doublesSet.getTeam2Score()));
		}
		return doublesSetResponseList;
	}

}
