package org.badminton.domain.club.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MemberTier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clubId;

	private String clubName;

	@Column(columnDefinition = "text")
	private String clubDescription;

	private String clubImage;

	private boolean isClubDeleted;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "club")
	private List<ClubMemberEntity> clubMembers = new ArrayList<>();

	public ClubEntity(String clubName, String clubDescription, String clubImage) {
		this.clubName = clubName;
		this.clubDescription = clubDescription;
		this.clubImage = clubImage;
		isClubDeleted = false;
	}

	public void updateClub(String clubName, String clubDescription, String clubImage) {
		this.clubName = clubName;
		this.clubDescription = clubDescription;
		this.clubImage = clubImage;
	}

	public Map<MemberTier, Long> getClubMemberCountByTier() {
		return clubMembers.stream()
			.filter(clubMember -> clubMember.getLeagueRecord() != null)
			.filter(clubMember -> !clubMember.isDeleted() && !clubMember.isBanned())
			.collect(Collectors.groupingBy(
				clubMember -> clubMember.getLeagueRecord().getTier(),
				Collectors.counting()
			));
	}

	public void doWithdrawal() {
		this.isClubDeleted = true;
	}
}
