package org.badminton.api.match.controller;

import java.util.List;

import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.api.match.service.MatchCreateService;
import org.badminton.api.match.service.MatchProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/clubs/{clubId}/leagues/{leagueId}/matches")
public class MatchController {

	private final MatchCreateService matchCreateService;
	private final MatchProgressService matchProgressService;

	@GetMapping
	@Operation(summary = "대진표 조회",
		description = "대진표를 조회합니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchResponse>> getMatches(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchResponse> matchResponseList = matchCreateService.getMatches(clubId, leagueId);
		return ResponseEntity.ok(matchResponseList);
	}

	@PostMapping
	@Operation(summary = "대진표 생성",
		description = "대진표를 생성합니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchResponse>> makeMatches(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchResponse> matchResponseList = matchCreateService.makeMatches(leagueId);
		return ResponseEntity.ok(matchResponseList);
	}

	@PostMapping("/details")
	@Operation(summary = "대진표 대진별, 세트별, 점수 초기화",
		tags = {"Match"})
	public ResponseEntity<List<MatchDetailsResponse>> makeMatchesDetails(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchDetailsResponse> matchDetailsResponseList = matchCreateService.initMatchDetails(clubId, leagueId);
		return ResponseEntity.ok(matchDetailsResponseList);
	}

	@PostMapping("/{matchId}/set/{setIndex}")
	@Operation(summary = "세트별 점수 저장",
		tags = {"Match"})
	public ResponseEntity<SetScoreUpdateResponse> updateSetsScore(
		@PathVariable Long clubId,
		@PathVariable Long leagueId,
		@PathVariable Long matchId,
		@PathVariable int setIndex,
		@Valid SetScoreUpdateRequest setScoreUpdateRequest
	) {
		SetScoreUpdateResponse setScoreUpdateResponse = matchProgressService.updateSetScore(clubId, leagueId, matchId,
			setIndex, setScoreUpdateRequest);
		return ResponseEntity.ok(setScoreUpdateResponse);
	}
}
