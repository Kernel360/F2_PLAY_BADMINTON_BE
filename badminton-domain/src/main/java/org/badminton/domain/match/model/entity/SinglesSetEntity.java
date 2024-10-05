package org.badminton.domain.match.model.entity;

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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "singlesMatchId")
	SinglesMatchEntity singlesMatch;

	private int set_index;
	private int player1Score;
	private int player2Score;

	public SinglesSetEntity(SinglesMatchEntity singlesMatch) {
		this.singlesMatch = singlesMatch;
		this.set_index = 0;
		this.player1Score = 0;
		this.player2Score = 0;
	}
}
