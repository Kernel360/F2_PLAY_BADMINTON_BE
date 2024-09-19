package org.badminton.api.match.model.dto;

import org.badminton.domain.match.entity.SinglesMatchEntity;

import io.swagger.v3.oas.annotations.media.Schema;

public record SinglesMatchCreateRequest(
	@Schema(description = "경기 아이디", example = "1L")
	Long leagueId,

	@Schema(description = "유저 아이디", example = "1L")
	Long memberId,

	@Schema(description = "상대방 아이디", example = "1L")
	Long opponentId,

	@Schema(description = "경기 결과", example = "WIN")
	String matchResult,

	@Schema(description = "나의 경기 스코어", example = "25L")
	Long myScore,

	@Schema(description = "상대방 경기 스코어", example = "14L")
	Long opponentScore,

	@Schema(description = "대진 회차", example = "2L")
	Long setIndex
) {
	public static SinglesMatchEntity singlesMatchCreateRequestToEntity(SinglesMatchCreateRequest request) {
		return new SinglesMatchEntity(
			request.leagueId,
			request.memberId,
			request.opponentId,
			request.matchResult,
			request.myScore,
			request.opponentScore,
			request.setIndex
		);
	}
}
