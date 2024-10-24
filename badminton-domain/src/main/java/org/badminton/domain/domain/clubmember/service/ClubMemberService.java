package org.badminton.domain.domain.clubmember.service;

import java.util.List;
import java.util.Map;

import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.badminton.domain.domain.clubmember.command.ClubMemberBanCommand;
import org.badminton.domain.domain.clubmember.command.ClubMemberExpelCommand;
import org.badminton.domain.domain.clubmember.command.ClubMemberRoleUpdateCommand;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.info.ClubMemberBanRecordInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberJoinInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberWithdrawInfo;

public interface ClubMemberService {

	ClubMemberJoinInfo joinClub(String memberToken, Long clubId);

	void createClubJoinInfo(String memberToken, ClubCreateInfo clubInfo);

	ClubMemberInfo updateClubMemberRole(ClubMemberRoleUpdateCommand request, Long clubMemberId);

	Map<ClubMember.ClubMemberRole, List<ClubMemberInfo>> findAllClubMembers(Long clubId);

	ClubMember getClubMember(String memberToken);

	ClubMember getClubMember(Long clubMemberId);

	ClubMemberBanRecordInfo expelClubMember(ClubMemberExpelCommand request, Long clubMemberId);

	ClubMemberBanRecordInfo banClubMember(ClubMemberBanCommand request, Long clubMemberId);

	ClubMemberWithdrawInfo withDrawClubMember(Long clubId, String memberToken);

	boolean checkIfMemberBelongsToClub(String memberToken, Long clubId);

	void checkMyOwnClub(String memberToken);

	void deleteAllClubMembers(Long clubId);
}


