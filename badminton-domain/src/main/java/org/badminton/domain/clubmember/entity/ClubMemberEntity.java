package org.badminton.domain.clubmember.entity;

import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.member.entity.MemberEntity;

import jakarta.persistence.Entity;
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
@Table(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubMemberEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clubMemberId;

	private boolean isDeleted;

	private String role;

	@ManyToOne
	@JoinColumn(name = "clubId")
	private ClubEntity club;

	@ManyToOne
	@JoinColumn(name = "memberId")
	private MemberEntity member;

	public ClubMemberEntity(ClubEntity club, MemberEntity member, ClubMemberRole role) {
		this.club = club;
		this.member = member;
		this.role = role.name();
		this.isDeleted = false;
	}
}
