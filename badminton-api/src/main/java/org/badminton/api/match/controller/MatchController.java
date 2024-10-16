package org.badminton.api.match.controller;

import java.util.List;

import org.badminton.api.match.model.dto.MatchDetailsResponse;
import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.api.match.model.dto.SetScoreUpdateRequest;
import org.badminton.api.match.model.dto.SetScoreUpdateResponse;
import org.badminton.api.match.service.MatchInitService;
import org.badminton.api.match.service.MatchProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	private final MatchInitService matchInitService;
	private final MatchProgressService matchProgressService;

	@GetMapping
	@Operation(summary = "대진표 조회",
		description = "대진표를 조회합니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchResponse>> getAllMatches(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchResponse> matchResponseList = matchInitService.getAllMatchesInLeague(clubId, leagueId);
		return ResponseEntity.ok(matchResponseList);
	}

	@GetMapping("/details")
	@Operation(summary = "대진표 상세 조회",
		description = "대진표를 상세 조회합니다. 모든 대진 정보와 대진별, 세트별 점수를 조회할 수 있습니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchDetailsResponse>> getAllMatchesDetails(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchDetailsResponse> matchDetailsResponseList = matchInitService.getAllMatchesDetailsInLeague(clubId,
			leagueId);
		return ResponseEntity.ok(matchDetailsResponseList);
	}

	@GetMapping("/{matchId}")
	@Operation(summary = "각 대진별 상세 조회",
		description = "각 대진별로 세트별 점수를 조회합니다.",
		tags = {"Match"})
	public ResponseEntity<MatchDetailsResponse> getMatchDetails(
		@PathVariable Long clubId,
		@PathVariable Long leagueId,
		@PathVariable Long matchId
	) {
		MatchDetailsResponse matchDetailsREsponse = matchInitService.getMatchDetailsInLeague(clubId, leagueId, matchId);
		return ResponseEntity.ok(matchDetailsREsponse);
	}

	@PostMapping
	@Operation(summary = "대진표 생성",
		description = "대진표를 생성합니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchResponse>> makeMatches(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchResponse> matchResponseList = matchInitService.makeMatches(leagueId);
		return ResponseEntity.ok(matchResponseList);
	}

	@PostMapping("/details")
	@Operation(summary = "대진표 대진별, 세트별, 점수 초기화",
		tags = {"Match"})
	public ResponseEntity<List<MatchDetailsResponse>> makeMatchesDetails(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchDetailsResponse> matchDetailsResponseList = matchInitService.initMatchDetails(clubId, leagueId);
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
		@Valid @RequestBody SetScoreUpdateRequest setScoreUpdateRequest
	) {
		SetScoreUpdateResponse setScoreUpdateResponse = matchProgressService.updateSetScore(clubId, leagueId, matchId,
			setIndex, setScoreUpdateRequest);
		return ResponseEntity.ok(setScoreUpdateResponse);
	}
}
