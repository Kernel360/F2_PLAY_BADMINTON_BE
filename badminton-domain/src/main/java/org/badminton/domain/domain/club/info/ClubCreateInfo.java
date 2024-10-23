package org.badminton.domain.domain.club.info;

import java.time.LocalDateTime;
import java.util.List;

import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public record ClubCreateInfo(
	Long clubId,
	String clubName,
	String clubDescription,
	String clubImage,
	boolean isClubDeleted,
	List<ClubMemberEntity> clubMembers,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

}
