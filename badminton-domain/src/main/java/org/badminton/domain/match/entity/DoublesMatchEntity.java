package org.badminton.domain.match.entity;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant1Id")
	private LeagueParticipantEntity leagueParticipant1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant2Id")
	private LeagueParticipantEntity leagueParticipant2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant3Id")
	private LeagueParticipantEntity leagueParticipant3;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "leagueParticipant4Id")
	private LeagueParticipantEntity leagueParticipant4;

	public DoublesMatchEntity(LeagueEntity league, LeagueParticipantEntity leagueParticipant1,
		LeagueParticipantEntity leagueParticipant2,
		LeagueParticipantEntity leagueParticipant3,
		LeagueParticipantEntity leagueParticipant4) {
		this.league = league;
		this.leagueParticipant1 = leagueParticipant1;
		this.leagueParticipant2 = leagueParticipant2;
		this.leagueParticipant3 = leagueParticipant3;
		this.leagueParticipant4 = leagueParticipant4;

	}
}
