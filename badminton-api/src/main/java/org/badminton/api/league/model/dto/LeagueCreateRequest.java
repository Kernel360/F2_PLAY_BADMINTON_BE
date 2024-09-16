package org.badminton.api.league.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class LeagueCreateRequest {

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	private String name;

	@Schema(description = "경기 설명", example = "이 경기는 지역 예선 경기입니다.")
	String description;

	@Schema(description = "최소 티어", example = "Gold")
	String tierLimit;

	@Schema(description = "현재 경기 상태", example = "Open")
	String status;

	@Schema(description = "경기 방식", example = "Single Elimination")
	String matchType;

	@Schema(description = "경기 시작 날짜", example = "2024-09-10T15:30:00")
	LocalDateTime leagueAt;

	@Schema(description = "모집 마감 날짜", example = "2024-09-08T23:59:59")
	LocalDateTime closedAt;

	@Schema(description = "참가 인원", example = "16")
	Long playerCount;

	@Schema(description = "생성 일자", example = "2024-08-01T12:00:00")
	LocalDateTime createdAt;

	@Schema(description = "수정 일자", example = "2024-08-15T12:00:00")
	LocalDateTime modifiedAt;
}
