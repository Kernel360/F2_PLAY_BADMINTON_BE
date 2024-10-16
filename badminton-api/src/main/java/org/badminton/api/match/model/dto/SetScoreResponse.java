package org.badminton.api.match.model.dto;

import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;

public record SetScoreResponse(
	Long matchId,
	int setIndex,
	int score1,
	int score2
) {

	public static SetScoreResponse fromSinglesSetEntity(Long matchId, int setIndex,
		SinglesSetEntity singlesSetEntity) {
		return new SetScoreResponse(matchId, setIndex, singlesSetEntity.getPlayer1Score(),
			singlesSetEntity.getPlayer2Score()
		);
	}

	public static SetScoreResponse fromDoublesSetEntity(Long matchId, int setIndex,
		DoublesSetEntity doublesSetEntity) {
		return new SetScoreResponse(matchId, setIndex, doublesSetEntity.getTeam1Score(),
			doublesSetEntity.getTeam2Score());
	}
}

