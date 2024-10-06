package org.badminton.api.match.model.dto;

import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.match.model.entity.DoublesSetEntity;
import org.badminton.domain.match.model.entity.SinglesSetEntity;

public record SetScoreUpdateResponse(
	int score1,
	int score2,
	MatchType matchType
) {

	public static SetScoreUpdateResponse singlesSetentityToSetScoreUpdateResponse(SinglesSetEntity singlesSetEntity) {
		return new SetScoreUpdateResponse(singlesSetEntity.getPlayer1Score(), singlesSetEntity.getPlayer2Score(),
			MatchType.SINGLES);
	}

	public static SetScoreUpdateResponse doublesSetEntityToSetScoreUpdateResponse(DoublesSetEntity doublesSetEntity) {
		return new SetScoreUpdateResponse(doublesSetEntity.getTeam1Score(), doublesSetEntity.getTeam2Score(),
			MatchType.DOUBLES);
	}
}
