package org.badminton.domain.domain.member.info;

import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.info.ClubMemberMyPageInfo;
import org.badminton.domain.domain.league.LeagueRecordInfo;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.domain.member.entity.Member;

public record MemberMyPageInfo(
	String memberToken,

	String name,

	String email,

	String profileImage,

	Member.MemberTier tier,

	LeagueRecordInfo leagueRecordInfo,

	ClubMemberMyPageInfo clubMemberMyPageInfo


) {
	public static MemberMyPageInfo from(Member member, LeagueRecord leagueRecord, ClubMember clubMember) {
		return new MemberMyPageInfo(
			member.getMemberToken(), member.getName(), member.getEmail(), member.getProfileImage(), member.getTier(),
			LeagueRecordInfo.toLeagueRecordInfo(leagueRecord), ClubMemberMyPageInfo.from(clubMember));

	}

	public static MemberMyPageInfo from(Member member, LeagueRecord leagueRecord) {
		return new MemberMyPageInfo(
			member.getMemberToken(), member.getName(), member.getEmail(), member.getProfileImage(), member.getTier(),
			LeagueRecordInfo.toLeagueRecordInfo(leagueRecord), null);

	}
}
