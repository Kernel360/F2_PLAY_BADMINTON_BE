package org.badminton.domain.clubmember.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "club_member_ban")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ClubMemberBanEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clubMemberBanId;

	private boolean isBanned;

	private String banType;

	private LocalDateTime banStartAt;
}
