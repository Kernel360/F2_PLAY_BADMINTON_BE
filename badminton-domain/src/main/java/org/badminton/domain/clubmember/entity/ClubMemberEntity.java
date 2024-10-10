package org.badminton.domain.clubmember.entity;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.member.entity.MemberEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubMemberEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clubMemberId;

	// TODO: 동호회 탈퇴 시
	// TODO: 추방, 강제 퇴장, 내보내기
	// TODO: Ban 한 Club Member Entity 추가
	private boolean deleted;

	private boolean banned;

	@Enumerated(EnumType.STRING)
	private ClubMemberRole role;

	@ManyToOne
	@JoinColumn(name = "clubId")
	private ClubEntity club;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private MemberEntity member;

	@OneToOne(mappedBy = "clubMember", fetch = FetchType.LAZY)
	private LeagueRecordEntity leagueRecord;

	public ClubMemberEntity(ClubEntity club, MemberEntity member, ClubMemberRole role) {
		this.club = club;
		this.member = member;
		this.role = role;
		this.deleted = false;
		this.banned = false;
	}

	public void withdrawal() {
		this.deleted = true;
	}

	public void ban() {
		this.banned = true;
	}
}
