package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "league")
@Schema(description = "경기 엔티티")
public class LeagueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "경기 ID", example = "1")
	Long leagueId;

	@Schema(description = "경기 이름", example = "배드민턴 경기")
	String name;

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

	public LeagueEntity(String name, String description, LocalDateTime leagueAt,
		String tierLimit, LocalDateTime closedAt, String status, Long playerCount,
		String matchType, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.name = name;
		this.description = description;
		this.leagueAt = leagueAt;
		this.tierLimit = tierLimit;
		this.closedAt = closedAt;
		this.status = status;
		this.playerCount = playerCount;
		this.matchType = matchType;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}
