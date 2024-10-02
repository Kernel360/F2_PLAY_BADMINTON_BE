package org.badminton.domain.match.model.entity;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.match.model.vo.Team;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.Embedded;
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

	public DoublesMatchEntity(LeagueEntity league, Team team1, Team team2) {
		this.league = league;
		this.team1 = team1;
		this.team2 = team2;
	}
}
