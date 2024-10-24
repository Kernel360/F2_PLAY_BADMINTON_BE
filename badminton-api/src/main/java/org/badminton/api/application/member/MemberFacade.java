package org.badminton.api.application.member;

import org.badminton.api.service.leaguerecord.LeagueRecordService;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.service.ClubMemberService;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.domain.member.info.MemberIsClubMemberInfo;
import org.badminton.domain.domain.member.info.MemberMyPageInfo;
import org.badminton.domain.domain.member.info.MemberUpdateInfo;
import org.badminton.domain.domain.member.service.MemberService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberFacade {

	private final MemberService memberService;
	private final LeagueRecordService leagueRecordService;
	private final ClubMemberService clubMemberService;

	public MemberIsClubMemberInfo getMemberIsClubMember(String memberToken) {
		ClubMember clubMember = clubMemberService.getClubMember(memberToken);
		return memberService.getMemberIsClubMember(memberToken, clubMember);
	}

	public MemberMyPageInfo getMemberMyPageInfo(String memberToken) {
		ClubMember clubMember = clubMemberService.getClubMember(memberToken);
		LeagueRecord leagueRecord = leagueRecordService.getLeagueRecord(memberToken);
		return memberService.getMemberInfo(memberToken,leagueRecord, clubMember);
	}

	public MemberUpdateInfo updateProfileImage(String memberToken, String imageUrl) {
		return memberService.updateProfileImage(memberToken, imageUrl);
	}
}
