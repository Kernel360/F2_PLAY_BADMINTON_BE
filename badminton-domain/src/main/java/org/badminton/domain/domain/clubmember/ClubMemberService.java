package org.badminton.domain.domain.clubmember;

import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.member.entity.MemberEntity;

public interface ClubMemberService {
	boolean checkIfMemberBelongsToClub(Long memberId, Long clubId);

	void checkMyOwnClub(Long memberId);

	ClubMemberEntity createClubMember(ClubCreateInfo clubCreateInfo, MemberEntity member, ClubMemberRole role);

	void deleteAllClubMembers(Long clubId);
}
