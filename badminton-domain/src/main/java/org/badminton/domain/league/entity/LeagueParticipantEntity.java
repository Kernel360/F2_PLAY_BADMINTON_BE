package org.badminton.domain.league.entity;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.member.entity.MemberEntity;

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
@Table(name = "league_participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LeagueParticipantEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leagueParticipantId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "clubMemberId")
	private ClubMemberEntity clubMember;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "memberId")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "leagueId")
	private LeagueEntity league;

	private boolean canceled;

	public LeagueParticipantEntity(ClubMemberEntity clubMember, LeagueEntity league) {
		this.clubMember = clubMember;
		this.league = league;
		this.canceled = false;
		this.member = clubMember.getMember();
	}

	public void cancelLeagueParticipation() {
		this.canceled = true;
	}
	
}
