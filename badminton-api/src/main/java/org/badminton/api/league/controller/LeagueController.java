package org.badminton.api.league.controller;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.service.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/league")
public class LeagueController {
	private final LeagueService leagueService;

	@Operation(
		summary = "경기를 생성합니다.",
		description = "경기 생성하고를 데이터베이스에 저장합니다.",
		tags = {"league"}
	)
	@PostMapping
	public ResponseEntity<LeagueCreateResponse> leagueCreate(@RequestBody LeagueCreateRequest request) {
		return ResponseEntity.ok(leagueService.createLeague(request));
	}
}
