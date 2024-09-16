package org.badminton.api.league;

import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.service.LeagueService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/league")
public class LeagueController {
	private final LeagueService leagueService;

	@Operation(
		summary = "경기를 생성합니다.",
		description = "경기를 데이터베이스에 저장합니다.",
		tags = {"League"}, // 태그 분류
		requestBody = @RequestBody(
			description = "경기 저장 시 필요한 데이터",
			required = true,
			content = @Content(
			)
		)
	)
	@PostMapping("")
	public void leagueCreate(@org.springframework.web.bind.annotation.RequestBody LeagueCreateRequest request) {
		leagueService.createLeague(request);
	}
}
