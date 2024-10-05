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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "doubles_match_result")
@NoArgsConstructor
public class DoublesMatchResultEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long singlesMatchResultId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "doublesMatchId")
	private DoublesMatchEntity doublesMatch;

	private int team1WinningSetCount;
	private int team2WinningSetCount;

	public DoublesMatchResultEntity(DoublesMatchEntity doublesMatch) {
		this.doublesMatch = doublesMatch;
		team1WinningSetCount = 0;
		team2WinningSetCount = 0;
	}
}
