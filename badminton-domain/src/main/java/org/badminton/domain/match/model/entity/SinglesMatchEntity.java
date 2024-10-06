package org.badminton.domain.match.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MatchResult;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;

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
@Table(name = "singles_match")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SinglesMatchEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long singlesMatchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueId")
	private LeagueEntity league;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant1Id")
	private LeagueParticipantEntity leagueParticipant1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant2Id")
	private LeagueParticipantEntity leagueParticipant2;

	private int player1WinSetCount;
	private int player2WinSetCount;

	@Enumerated(EnumType.STRING)
	private MatchResult player1MatchResult = MatchResult.NONE;

	@Enumerated(EnumType.STRING)
	private MatchResult player2MatchResult = MatchResult.NONE;

	@OneToMany(mappedBy = "singlesMatch")
	private List<SinglesSetEntity> singlesSets;

	public SinglesMatchEntity(LeagueEntity league, LeagueParticipantEntity leagueParticipant1,
		LeagueParticipantEntity leagueParticipant2) {
		this.league = league;
		this.leagueParticipant1 = leagueParticipant1;
		this.leagueParticipant2 = leagueParticipant2;
		this.singlesSets = new ArrayList<>();
		this.player1WinSetCount = 0;
		this.player2WinSetCount = 0;
	}

	public void addSet(SinglesSetEntity singlesSet) {
		this.singlesSets.add(singlesSet);
	}

	public void player1WinSet() {
		this.player1WinSetCount++;
		if (player1WinSetCount == 2) {
			this.player1MatchResult = MatchResult.WIN;
			this.player2MatchResult = MatchResult.LOSE;
		}
	}

	public void player2WinSet() {
		this.player2WinSetCount++;
		if (player2WinSetCount == 2) {
			this.player2MatchResult = MatchResult.WIN;
		}
	}
}