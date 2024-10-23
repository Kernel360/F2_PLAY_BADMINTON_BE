package org.badminton.domain.domain.clubmember;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.member.entity.MemberEntity;

public interface ClubMemberStore {
	ClubMemberEntity createClubMember(Club club, MemberEntity memberId, ClubMemberRole role);

	void store(ClubMemberEntity clubMember);
}
