package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.league.enums.LeagueStatus;

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

	private MemberTier tierLimit;

	private LeagueStatus status;

	private MatchType matchType;

	private LocalDateTime leagueAt;

	private LocalDateTime closedAt;

	private Long playerCount;

	private String matchingRequirement;

	public LeagueEntity(String leagueName, String description, LocalDateTime leagueAt,
		MemberTier tierLimit, LocalDateTime closedAt, LeagueStatus status, Long playerCount,
		MatchType matchType, String matchingRequirement) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueAt = leagueAt;
		this.tierLimit = tierLimit;
		this.closedAt = closedAt;
		this.status = status;
		this.playerCount = playerCount;
		this.matchType = matchType;
		this.matchingRequirement = matchingRequirement;
	}

	public static LeagueEntity updateLeague(Long leagueId, String leagueName, String description,
		LocalDateTime leagueAt,
		MemberTier tierLimit, LocalDateTime closedAt, LeagueStatus status, Long playerCount,
		MatchType matchType, String matchingRequirement) {
		return new LeagueEntity(leagueId, leagueName, description, tierLimit, status, matchType, leagueAt, closedAt,
			playerCount, matchingRequirement);
	}

}
