package org.badminton.api.match.controller;

import org.badminton.api.match.service.SinglesMatchService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/match")
public class SinglesMatchController {
	private final SinglesMatchService singlesMatchService;

	@Operation(
		summary = "싱글 매칭 대진표를 생성합니다..",
		description = "싱글 매칭 대진표를 생성하여 저장합니다.",
		tags = {"match"}
	)
	@PostMapping("{leagueId}")
	public void singlesMatchCreate(@PathVariable("leagueId") Long leagueId) {
		singlesMatchService.singlesMatchCreate(leagueId);
	}
}
