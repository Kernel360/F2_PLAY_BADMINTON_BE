package org.badminton.domain.domain.member.service;

import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.domain.member.info.MemberInfo;
import org.badminton.domain.domain.member.info.MemberIsClubMemberInfo;
import org.badminton.domain.domain.member.info.MemberMyPageInfo;
import org.badminton.domain.domain.member.info.MemberUpdateInfo;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public interface MemberService {

	MemberIsClubMemberInfo getMemberIsClubMember(String memberToken, ClubMember clubMember);
	MemberMyPageInfo getMemberInfo(String memberToken , LeagueRecord leagueRecord , ClubMember clubMember);
	MemberUpdateInfo updateProfileImage(String memberToken, String imageUrl);
}
