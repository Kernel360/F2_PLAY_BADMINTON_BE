package org.badminton.domain.match.model.entity;

import static org.badminton.domain.common.consts.Constants.*;

import org.badminton.domain.common.BaseTimeEntity;

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
@Table(name = "singles_set")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SinglesSetEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long singlesSetId;
	/*
	league, match, set, league participant 사이의 연관관계가 너무 복잡한데 이를 어떻게 개선해야 할지
	감이 오지 않습니다.
	연관관계를 끊고 id를 필드로 갖는 방법을 사용해보는게 좋을까요?
	*/
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "singlesMatchId")
	SinglesMatchEntity singlesMatch;

	private int setIndex;
	private int player1Score;
	private int player2Score;

	public SinglesSetEntity(SinglesMatchEntity singlesMatch, int setIndex) {
		this.singlesMatch = singlesMatch;
		this.setIndex = setIndex;
		this.player1Score = INITIAL_SET_SCORE;
		this.player2Score = INITIAL_SET_SCORE;
	}

	public void saveSetScore(int player1Score, int player2Score) {
		this.player1Score = player1Score;
		this.player2Score = player2Score;
	}
}
