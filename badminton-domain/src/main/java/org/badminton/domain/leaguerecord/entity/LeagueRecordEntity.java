package org.badminton.domain.leaguerecord.entity;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.BaseTimeEntity;
import org.badminton.domain.common.enums.MemberTier;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "league_record")
public class LeagueRecordEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leagueRecordId;

	private int winCount;

	private int loseCount;

	private int drawCount;

	private int matchCount;

	@Enumerated(EnumType.STRING)
	private MemberTier tier;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubMemberId")
	private ClubMemberEntity clubMember;

	public void addWind() {
		this.winCount++;
		this.matchCount++;
		updateTier();
	}

	public void addLose() {
		this.loseCount++;
		this.matchCount++;
		updateTier();
	}

	public void addDraw() {
		this.drawCount++;
		this.matchCount++;
		updateTier();
	}

	public double getWinRate() {
		if (matchCount == 0) {
			return 0.0;
		}
		return (double)winCount / matchCount * 100;
	}

	private void updateTier() {
		double winRate = getWinRate();
		if (matchCount >= 20 && winRate >= 80) {
			tier = MemberTier.GOLD;
		} else if (matchCount >= 10 && matchCount < 20 && winRate >= 60) {
			tier = MemberTier.SILVER;
		} else {
			tier = MemberTier.BRONZE;
		}
	}

	public LeagueRecordEntity(ClubMemberEntity clubMember) {
		this.clubMember = clubMember;
		this.winCount = 0;
		this.loseCount = 0;
		this.drawCount = 0;
		this.matchCount = 0;
		this.tier = MemberTier.BRONZE;
	}

}
