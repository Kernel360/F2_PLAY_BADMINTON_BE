package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

import org.badminton.domain.common.BaseTimeEntity;

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
@Entity
@Table(name = "league")
public class LeagueEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leagueId;

	private String leagueName;

	private String description;

	private String tierLimit;

	private String status;

	private String matchType;

	private LocalDateTime leagueAt;

	private LocalDateTime closedAt;

	private Long playerCount;

	public LeagueEntity(String leagueName, String description, LocalDateTime leagueAt,
		String tierLimit, LocalDateTime closedAt, String status, Long playerCount,
		String matchType) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueAt = leagueAt;
		this.tierLimit = tierLimit;
		this.closedAt = closedAt;
		this.status = status;
		this.playerCount = playerCount;
		this.matchType = matchType;
	}
}
