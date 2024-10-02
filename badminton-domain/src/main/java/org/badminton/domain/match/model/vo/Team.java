package org.badminton.domain.match.model.vo;

import org.badminton.domain.league.entity.LeagueParticipantEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Embeddable
@Getter
public class Team {

	@ManyToOne(fetch = FetchType.LAZY)
	private LeagueParticipantEntity leagueParticipant1;

	@ManyToOne(fetch = FetchType.LAZY)
	private LeagueParticipantEntity leagueParticipant2;

	protected Team() {
	}

	public Team(LeagueParticipantEntity leagueParticipant1, LeagueParticipantEntity leagueParticipant2) {
		this.leagueParticipant1 = leagueParticipant1;
		this.leagueParticipant2 = leagueParticipant2;
	}
}