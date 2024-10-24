package org.badminton.domain.domain.clubmember;

import java.util.List;

import org.badminton.domain.domain.clubmember.entity.ClubMember;

public interface ClubMemberReader {
	ClubMember getClubMemberByMemberToken(String memberToken);
	ClubMember getClubMember(Long clubMemberId);
	List<ClubMember> getClubMembersByMemberToken(String memberToken);
	boolean isExist(String memberToken);
	boolean existsMemberInClub(String memberToken, Long clubId);
	List<ClubMember> getAllClubMemberByClubId(Long clubId);

	boolean checkIsClubMember(String memberToken, Long clubId);

	void checkIsClubOwner(String memberToken);

	List<ClubMember> getAllMember(Long clubId);
}
