package org.badminton.domain.league.entity;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.member.entity.MemberEntity;

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
	private Long leagueParticipantId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "league_id")
	private LeagueEntity league;

	private boolean isCanceled;

	public LeagueParticipationEntity(MemberEntity member, LeagueEntity league) {
		this.member = member;
		this.league = league;
		this.isCanceled = false;
	}

	public void cancelLeagueParticipation() {
		this.isCanceled = true;
	}

}
