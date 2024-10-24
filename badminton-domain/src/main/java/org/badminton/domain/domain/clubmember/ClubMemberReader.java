package org.badminton.domain.domain.clubmember;

import java.util.List;

import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public interface ClubMemberReader {
	boolean checkIsClubMember(Long memberId, Long clubId);

	void checkIsClubOwner(Long memberId);

	List<ClubMemberEntity> getAllMember(Long clubId);
}
