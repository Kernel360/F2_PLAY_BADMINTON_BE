package org.badminton.domain.match.entity;

import org.badminton.domain.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "singles_match")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SinglesMatchEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long singlesMatchId;

	private Long leagueId;

	private Long memberId;

	private Long opponentId;

	private String matchResult;

	private Long myScore;

	private Long opponentScore;

	private Long setIndex;

	public SinglesMatchEntity(Long leagueId, Long memberId, Long opponentId, String matchResult, Long myScore,
		Long opponentScore, Long setIndex) {
		this.leagueId = leagueId;
		this.memberId = memberId;
		this.opponentId = opponentId;
		this.matchResult = matchResult;
		this.myScore = myScore;
		this.opponentScore = opponentScore;
		this.setIndex = setIndex;
	}
}
