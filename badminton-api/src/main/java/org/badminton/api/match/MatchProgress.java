package org.badminton.api.match;

import java.util.List;

import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;

public interface MatchProgress {

	List<MatchResponse> getAllMatchesInLeague(Long leagueId);

	List<MatchDetailsResponse> getAllMatchesDetailsInLeague(Long leagueId);

	MatchDetailsResponse getMatchDetails(Long matchId);

	List<MatchResponse> makeMatches(LeagueEntity league, List<LeagueParticipantEntity> leagueParticipantList);

	List<MatchDetailsResponse> initDetails(Long leagueId);

	SetScoreUpdateResponse updateSetScore(Long matchId, int setIndex, SetScoreUpdateRequest setScoreUpdateRequest);

	void checkDuplicateMatchInLeague(Long leagueId, MatchType matchType);
}
