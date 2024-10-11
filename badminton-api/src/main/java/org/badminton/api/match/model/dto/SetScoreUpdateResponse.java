package org.badminton.api.match.model.dto;

import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;

public record SetScoreUpdateResponse(
	Long matchId,
	int setIndex,
	int score1,
	int score2,
	MatchType matchType
) {

	public static SetScoreUpdateResponse singlesSetentityToSetScoreUpdateResponse(Long matchId, int setIndex,
		SinglesSetEntity singlesSetEntity) {
		return new SetScoreUpdateResponse(matchId, setIndex, singlesSetEntity.getPlayer1Score(),
			singlesSetEntity.getPlayer2Score(),
			MatchType.SINGLES);
	}

	public static SetScoreUpdateResponse doublesSetEntityToSetScoreUpdateResponse(Long matchId, int setIndex,
		DoublesSetEntity doublesSetEntity) {
		return new SetScoreUpdateResponse(matchId, setIndex, doublesSetEntity.getTeam1Score(),
			doublesSetEntity.getTeam2Score(),
			MatchType.DOUBLES);
	}
}
