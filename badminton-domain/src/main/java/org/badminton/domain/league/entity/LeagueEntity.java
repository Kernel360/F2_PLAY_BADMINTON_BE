package org.badminton.domain.league.entity;

import java.time.LocalDateTime;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MatchGenerationType;
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

	private String leagueLocation;

	@Enumerated(EnumType.STRING)
	private MemberTier requiredTier;

	@Enumerated(EnumType.STRING)
	private LeagueStatus leagueStatus;

	@Enumerated(EnumType.STRING)
	private MatchType matchType;

	// TODO: Convert 등 사용하여 날짜 형식 맞추기
	private LocalDateTime leagueAt;

	private LocalDateTime recruitingClosedAt;

	private int playerLimitCount;

	@Enumerated(EnumType.STRING)
	private MatchGenerationType matchGenerationType;  //RANDOM, TIER, AGE

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubId")
	private ClubEntity club;

	public LeagueEntity(String leagueName, String description, String leagueLocation, LocalDateTime leagueAt,
		MemberTier tierLimit, LocalDateTime recruitingClosedAt, int playerLimitCount,
		MatchType matchType, MatchGenerationType matchGenerationType, ClubEntity club) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueLocation = leagueLocation;
		this.leagueAt = leagueAt;
		this.requiredTier = tierLimit;
		this.recruitingClosedAt = recruitingClosedAt;
		this.leagueStatus = LeagueStatus.RECRUITING;
		this.playerLimitCount = playerLimitCount;
		this.matchType = matchType;
		this.matchGenerationType = matchGenerationType;
		this.club = club;
	}

	public void updateLeague(String leagueName, String description, String leagueLocation, MemberTier requiredTier,
		LocalDateTime leagueAt,
		LocalDateTime closedAt, int playerLimitCount, MatchType matchType,
		MatchGenerationType matchGenerationType) {
		this.leagueName = leagueName;
		this.description = description;
		this.leagueLocation = leagueLocation;
		this.leagueAt = leagueAt;
		this.requiredTier = requiredTier;
		this.recruitingClosedAt = closedAt;
		this.playerLimitCount = playerLimitCount;
		this.matchType = matchType;
		this.matchGenerationType = matchGenerationType;

	}

	public void completeLeagueRecruiting() {
		this.leagueStatus = LeagueStatus.COMPLETED;
	}

	public void cancelLeague() {
		this.leagueStatus = LeagueStatus.CANCELED;
	}
}
