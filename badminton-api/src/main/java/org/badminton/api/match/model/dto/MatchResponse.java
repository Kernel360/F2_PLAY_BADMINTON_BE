package org.badminton.api.match.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchResponse {
	private Long matchId;
	private Long leagueId;
	private MatchType matchType;
	private LocalDateTime createAt;
	private LocalDateTime modifiedAt;

}
