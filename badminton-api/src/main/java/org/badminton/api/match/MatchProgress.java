package org.badminton.api.match;

import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;

import java.util.List;

public interface MatchProgress {
    List<MatchDetailsResponse> initDetails(Long leagueId);
    SetScoreUpdateResponse updateSetScore(Long matchId, int setIndex, SetScoreUpdateRequest setScoreUpdateRequest);
}
