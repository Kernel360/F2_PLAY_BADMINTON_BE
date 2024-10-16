package org.badminton.api.match.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.badminton.domain.match.model.entity.SinglesSetEntity;

public record SinglesSetResponse(

	int setIndex,
	int score1,
	int score2
) {

	public static List<SinglesSetResponse> entityToSinglesSetResponse(List<SinglesSetEntity> singlesSets) {
		List<SinglesSetResponse> singlesSetResponseList = new ArrayList<>();
		for (SinglesSetEntity singlesSet : singlesSets) {
			singlesSetResponseList.add(new SinglesSetResponse(singlesSet.getSetIndex(), singlesSet.getPlayer1Score(),
				singlesSet.getPlayer2Score()));
		}
		return singlesSetResponseList;
	}
}
