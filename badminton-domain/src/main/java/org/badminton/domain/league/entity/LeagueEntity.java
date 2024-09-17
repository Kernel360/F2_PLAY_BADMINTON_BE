package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

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
public class LeagueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long leagueId;

	String name;

	String description;

	String tierLimit;

	String status;

	String matchType;

	LocalDateTime leagueAt;

	LocalDateTime closedAt;

	Long playerCount;

	LocalDateTime createdAt;

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
