package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MatchType;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.league.enums.LeagueStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@Enumerated(EnumType.STRING)
	private MemberTier tierLimit;

	@Enumerated(EnumType.STRING)
	private LeagueStatus leagueStatus;

	@Enumerated(EnumType.STRING)
	private MatchType matchType;

	// TODO: Convert 등 사용하여 날짜 형식 맞추기
	private LocalDateTime leagueAt;

	private LocalDateTime closedAt;

	private int playerCount;

	private String matchingRequirement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubId")
	private ClubEntity club;

	public LeagueEntity(String leagueName, String description, LocalDateTime leagueAt,
		MemberTier tierLimit, LocalDateTime closedAt, LeagueStatus leagueStatus, int playerCount,
		MatchType matchType, String matchingRequirement, ClubEntity club) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueAt = leagueAt;
		this.tierLimit = tierLimit;
		this.closedAt = closedAt;
		this.leagueStatus = leagueStatus;
		this.playerCount = playerCount;
		this.matchType = matchType;
		this.matchingRequirement = matchingRequirement;
		this.club = club;
	}

	public void updateLeague(String leagueName, String description, MemberTier tierLimit, LocalDateTime leagueAt,
		LocalDateTime closedAt, LeagueStatus leagueStatus, int playerCount, MatchType matchType,
		String matchingRequirement) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueAt = leagueAt;
		this.tierLimit = tierLimit;
		this.closedAt = closedAt;
		this.leagueStatus = leagueStatus;
		this.playerCount = playerCount;
		this.matchType = matchType;
		this.matchingRequirement = matchingRequirement;

	}
}
