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
@Table(name = "singles_match_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SinglesMatchResultEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long singlesMatchResultId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "singlesMatchId")
	private SinglesMatchEntity singlesMatch;

	private int player1WinningSetCount;
	private int player2WinningSetCount;

	public SinglesMatchResultEntity(SinglesMatchEntity singlesMatch) {
		this.singlesMatch = singlesMatch;
		player1WinningSetCount = 0;
		player2WinningSetCount = 0;
	}
}
