package org.badminton.domain.domain.club;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

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
@Getter
@Table(name = "club")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clubId;

	private String clubName;

	@Column(columnDefinition = "text")
	private String clubDescription;

	private String clubImage;

	private boolean isClubDeleted;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "club")
	private final List<ClubMemberEntity> clubMembers = new ArrayList<>();

	public Club(String clubName, String clubDescription, String clubImage) {
		super();
		this.clubName = clubName;
		this.clubDescription = clubDescription;
		this.clubImage = clubImage;
		isClubDeleted = false;
	}

	public void updateClub(ClubUpdateCommand clubUpdateCommand) {
		this.clubName = clubUpdateCommand.clubName();
		this.clubDescription = clubUpdateCommand.clubDescription();
		this.clubImage = clubUpdateCommand.clubImage();
	}

	public Map<MemberTier, Long> getClubMemberCountByTier() {

		List<MemberTier> tierListForInit = Arrays.asList(MemberTier.BRONZE, MemberTier.SILVER, MemberTier.GOLD);
		Map<MemberTier, Long> tierCounts = new LinkedHashMap<>();

		for (MemberTier tier : tierListForInit) {
			tierCounts.put(tier, 0L);
		}

		Map<MemberTier, Long> actualCounts = clubMembers.stream()
			.filter(clubMember -> clubMember.getLeagueRecord() != null)
			.filter(clubMember -> !clubMember.isDeleted())
			.collect(Collectors.groupingBy(
				ClubMemberEntity::getTier,
				Collectors.counting()
			));

		tierCounts.putAll(actualCounts);

		return tierCounts;
	}

	public void doWithdrawal() {
		this.isClubDeleted = true;
	}
}
