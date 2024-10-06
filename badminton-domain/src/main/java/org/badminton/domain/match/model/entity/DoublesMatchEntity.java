package org.badminton.domain.match.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MatchResult;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.match.model.vo.Team;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doubles_match")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DoublesMatchEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doublesMatchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueId")
	private LeagueEntity league;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "leagueParticipant1", joinColumns = @JoinColumn(name = "team1Participant1Id")),
		@AssociationOverride(name = "leagueParticipant2", joinColumns = @JoinColumn(name = "team1Participant2Id"))
	})
	private Team team1;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "leagueParticipant1", joinColumns = @JoinColumn(name = "team2Participant1Id")),
		@AssociationOverride(name = "leagueParticipant2", joinColumns = @JoinColumn(name = "team2Participant2Id"))
	})
	private Team team2;

	private int team1WinSetCount;
	private int team2WinSetCount;

	@Enumerated(EnumType.STRING)
	private MatchResult team1MatchResult = MatchResult.NONE;

	@Enumerated(EnumType.STRING)
	private MatchResult team2MatchResult = MatchResult.NONE;

	@OneToMany(mappedBy = "doublesMatch")
	List<DoublesSetEntity> doublesSets;

	public DoublesMatchEntity(LeagueEntity league, Team team1, Team team2) {
		this.league = league;
		this.team1 = team1;
		this.team2 = team2;
		this.doublesSets = new ArrayList<>();
		this.team1WinSetCount = 0;
		this.team2WinSetCount = 0;
	}

	public void addSet(DoublesSetEntity doublesSet) {
		this.doublesSets.add(doublesSet);
	}

	public void team1WinSet() {
		this.team1WinSetCount++;
		if (team1WinSetCount == 2) {
			this.team1MatchResult = MatchResult.WIN;
			this.team2MatchResult = MatchResult.LOSE;
		}
	}

	public void player2WinSet() {
		this.team2WinSetCount++;
		if (team2WinSetCount == 2) {
			this.team2MatchResult = MatchResult.WIN;
			this.team1MatchResult = MatchResult.LOSE;
		}
	}
}
