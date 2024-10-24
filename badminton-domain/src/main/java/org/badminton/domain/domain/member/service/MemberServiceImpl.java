package org.badminton.domain.domain.member.service;

import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.domain.member.MemberReader;
import org.badminton.domain.domain.member.MemberStore;
import org.badminton.domain.domain.member.entity.Member;
import org.badminton.domain.domain.member.info.MemberIsClubMemberInfo;
import org.badminton.domain.domain.member.info.MemberMyPageInfo;
import org.badminton.domain.domain.member.info.MemberUpdateInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberReader memberReader;
    private final MemberStore memberStore;

    @Override
    public MemberIsClubMemberInfo getMemberIsClubMember(String memberToken, ClubMember clubMember) {

        if (clubMember == null) {
            return new MemberIsClubMemberInfo(false, null, null);
        } else {
            ClubMember.ClubMemberRole clubMemberRole = clubMember.getRole();
            Long clubId = clubMember.getClub().getClubId();
            return new MemberIsClubMemberInfo(true, clubMemberRole, clubId);
        }
    }

    @Override
    public MemberMyPageInfo getMemberInfo(String memberToken, LeagueRecord leagueRecord, ClubMember clubMember) {
        Member member = memberReader.getMember(memberToken);
        return createMemberMyPageInfo(member, clubMember, leagueRecord);
    }

    private MemberMyPageInfo createMemberMyPageInfo(Member member, ClubMember clubMember, LeagueRecord leagueRecord) {
        if (!hasClubMember(clubMember)) {
            return MemberMyPageInfo.from(member,leagueRecord);
        }

        return MemberMyPageInfo.from(member, leagueRecord, clubMember);
    }

    private boolean hasClubMember(ClubMember clubMember) {
        return clubMember != null;
    }

    @Override
    @Transactional
    public MemberUpdateInfo updateProfileImage(String memberToken, String imageUrl) {
        Member member = memberReader.getMember(memberToken);
        member.updateMember(imageUrl);
        memberStore.store(member);
        return MemberUpdateInfo.fromMemberEntity(member);
    }

}
