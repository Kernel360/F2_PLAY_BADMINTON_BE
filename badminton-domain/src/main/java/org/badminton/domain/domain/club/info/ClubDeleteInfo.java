package org.badminton.domain.domain.club.info;

import java.time.LocalDateTime;
import java.util.List;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public record ClubDeleteInfo(
	Long clubId,
	String clubName,
	String clubDescription,
	String clubImage,
	boolean isClubDeleted,
	List<ClubMemberEntity> clubMembers,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public static ClubDeleteInfo clubDeleteInfo(Club club) {
		return new ClubDeleteInfo(
			club.getClubId(),
			club.getClubName(),
			club.getClubDescription(),
			club.getClubImage(),
			club.isClubDeleted(),
			club.getClubMembers(),
			club.getCreatedAt(),
			club.getModifiedAt());
	}
}
