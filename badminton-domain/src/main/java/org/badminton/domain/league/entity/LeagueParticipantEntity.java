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

	/*
	league, match, set, league participant 사이의 연관관계가 너무 복잡한데 이를 어떻게 개선해야 할지
	감이 오지 않습니다.
	연관관계를 끊고 id를 필드로 갖는 방법을 사용해보는게 좋을까요?
	*/
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
