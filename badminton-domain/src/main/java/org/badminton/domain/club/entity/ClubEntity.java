package org.badminton.domain.club.entity;

import org.badminton.domain.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private Long id;

	private String name;

	@Column(columnDefinition = "text")
	private String description;

	private String clubImage;

	public ClubEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
