package org.badminton.api.match.controller;

import java.util.List;

import org.badminton.api.match.model.dto.MatchResponse;
import org.badminton.api.match.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/club/{clubId}/league/{leagueId}/match")
public class MatchController {

	private final MatchService matchService;

	@PostMapping
	@Operation(summary = "대진표 생성",
		description = "대진표를 생성합니다.",
		tags = {"Match"})
	public ResponseEntity<List<MatchResponse>> makeMatches(
		@PathVariable Long clubId,
		@PathVariable Long leagueId
	) {
		List<MatchResponse> matchResponseList = matchService.makeMatches(leagueId);
		return ResponseEntity.ok(matchResponseList);
	}
}
