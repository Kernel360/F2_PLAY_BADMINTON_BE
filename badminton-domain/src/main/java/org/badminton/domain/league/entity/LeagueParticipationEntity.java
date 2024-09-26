package org.badminton.domain.league.entity;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.BaseTimeEntity;

import jakarta.persistence.CascadeType;
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
@Table(name = "league_participation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LeagueParticipationEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leagueParticipantionId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "clubMemberId")
	private ClubMemberEntity clubMember;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "leagueId")
	private LeagueEntity league;

	private boolean isCanceled;

	public LeagueParticipationEntity(ClubMemberEntity clubMember, LeagueEntity league) {
		this.clubMember = clubMember;
		this.league = league;
		this.isCanceled = false;
	}

	public void cancelLeagueParticipation() {
		this.isCanceled = true;
	}

	public void reactiveParticipation() {
		this.isCanceled = false;
	}

}
