package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import org.badminton.domain.common.enums.MatchGenerationType;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.enums.LeagueStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record LeagueAndParticipantResponse(
	@Schema(description = "경기 아이디", example = "1L")
	Long leagueId,

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	String leagueName,

	@Schema(description = "경기 설명", example = "이 경기는 지역 예선 경기입니다.")
	String description,

	@Schema(description = "최소 티어", example = "GOLD")
	MemberTier tierLimit,

	@Schema(description = "현재 경기 상태", example = "OPEN")
	LeagueStatus status,

	@Schema(description = "경기 방식", example = "SINGLE")
	MatchType matchType,

	@Schema(description = "경기 시작 날짜", example = "2024-09-10T15:30:00")
	LocalDateTime leagueAt,

	@Schema(description = "모집 마감 날짜", example = "2024-09-08T23:59:59")
	LocalDateTime closedAt,

	@Schema(description = "참가 제한 인원", example = "16")
	int playerLimitCount,

	@Schema(description = "생성 일자", example = "2024-09-10T15:30:00")
	LocalDateTime createdAt,

	@Schema(description = "수정 일자", example = "2024-09-10T15:30:00")
	LocalDateTime modifiedAt,

	@Schema(description = "매칭 조건", example = "RANDOM")
	MatchGenerationType matchGenerationType,

	@Schema(description = "현재 참여 인원", example = "0")
	int playerCount
) {
	public LeagueAndParticipantResponse(LeagueEntity entity, int playerCount) {
		this(entity.getLeagueId(),
			entity.getLeagueName(),
			entity.getDescription(),
			entity.getRequiredTier(),
			entity.getLeagueStatus(),
			entity.getMatchType(),
			entity.getLeagueAt(),
			entity.getClosedAt(),
			entity.getPlayerLimitCount(),
			entity.getCreatedAt(),
			entity.getModifiedAt(),
			entity.getMatchGenerationType(),
			playerCount
		);
	}

	public static LeagueAndParticipantResponse leagueAndParticipantEntityToResponse(LeagueEntity entity,
		int playerCount) {
		return new LeagueAndParticipantResponse(entity, playerCount);
	}
}
