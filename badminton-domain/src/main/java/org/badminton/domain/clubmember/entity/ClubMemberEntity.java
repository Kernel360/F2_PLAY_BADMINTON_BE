package org.badminton.domain.clubmember.entity;

import java.util.ArrayList;
import java.util.List;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.member.entity.MemberEntity;

import jakarta.persistence.CascadeType;
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

	@OneToMany(mappedBy = "clubMember", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ClubMemberBanRecordEntity> banHistory = new ArrayList<>();

	public ClubMemberEntity(ClubEntity club, MemberEntity member, ClubMemberRole role) {
		this.club = club;
		this.member = member;
		this.role = role;
		this.deleted = false;
		this.banned = false;
	}

	public void updateClubMemberRole(ClubMemberRole role) {
		this.role = role;
	}

	public void deleteClubMember() {
		this.deleted = true;
	}

	public void expel() {
		this.deleted = true;
		this.banned = true;
	}

	public void addBanRecord(ClubMemberBanRecordEntity clubMemberBanRecordEntity) {
		this.banHistory.add(clubMemberBanRecordEntity);
		this.banned = true;
	}


}
