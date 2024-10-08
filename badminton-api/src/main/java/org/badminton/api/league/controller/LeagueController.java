package org.badminton.api.league.controller;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueStatusUpdateResponse;
import org.badminton.api.league.model.dto.LeagueUpdateRequest;
import org.badminton.api.league.service.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/clubs/{clubId}/leagues")
public class LeagueController {
	private final LeagueService leagueService;

	@Operation(
		summary = "경기를 생성합니다.",
		description = "경기 생성하고를 데이터베이스에 저장합니다.",
		tags = {"league"}
	)
	@PostMapping
	public ResponseEntity<LeagueCreateResponse> createLeague(
		@PathVariable Long clubId,
		@Valid @RequestBody LeagueCreateRequest leagueCreateRequest) {
		return ResponseEntity.ok(leagueService.createLeague(clubId, leagueCreateRequest));
	}

	@Operation(
		summary = "특정 경기를 조회합니다.",
		description = "특정 경기를 경기 아이디를 통해 데이터베이스에서 조회합니다.",
		tags = {"league"}
	)
	@GetMapping("/{leagueId}")
	public ResponseEntity<LeagueReadResponse> leagueRead(@PathVariable Long clubId, @PathVariable Long leagueId) {
		return ResponseEntity.ok(leagueService.getLeague(clubId, leagueId));
	}

	@Operation(
		summary = "경기의 세부 정보를 변경합니다.",
		description = "경기 제목, 경기 상태 등을 변경할 수 있습니다.",
		tags = {"league"}
	)
	@PatchMapping("/{leagueId}")
	public ResponseEntity<LeagueStatusUpdateResponse> updateLeague(
		@PathVariable Long clubId,
		@PathVariable Long leagueId,
		@Valid @RequestBody LeagueUpdateRequest leagueUpdateRequest) {
		return ResponseEntity.ok(leagueService.updateLeague(clubId, leagueId, leagueUpdateRequest));
	}

	@Operation(
		summary = "특정 경기를 삭제합니다.",
		description = "특정 경기를 데이터베이스 테이블에서 제거합니다.",
		tags = {"league"}
	)

	@DeleteMapping("/{leagueId}")
	public ResponseEntity<HttpStatus> deleteLeague(
		@PathVariable Long clubId,
		@PathVariable Long leagueId) {
		leagueService.deleteLeague(clubId, leagueId);
		return ResponseEntity.ok(HttpStatus.OK);
	}

}
