package org.badminton.api.service.leaguerecord;

import org.badminton.domain.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.domain.domain.clubmember.ClubMemberReader;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.member.MemberReader;
import org.badminton.domain.domain.member.entity.Member;
import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
import org.badminton.domain.domain.league.entity.LeagueRecord;
import org.badminton.domain.infrastructures.league.LeagueRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueRecordService {

    private final LeagueRecordRepository leagueRecordRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberReader memberReader;
    private static final int GOLD_TIER_MIN_MATCHES = 20;
    private static final int GOLD_TIER_MIN_WIN_RATE = 80;
    private static final int SILVER_TIER_MIN_MATCHES = 10;
    private static final int SILVER_TIER_MIN_WIN_RATE = 60;

    @Transactional
    public void addWin(String memberToken) {
        LeagueRecord record = getLeagueRecord(memberToken);
        record.updateWinCount(record.getWinCount() + 1);
        record.updateMatchCount(record.getMatchCount() + 1);
        updateTier(memberToken);
        leagueRecordRepository.save(record);
    }

    @Transactional
    public void addLose(String memberToken) {
        LeagueRecord record = getLeagueRecord(memberToken);
        record.updateLoseCount(record.getLoseCount() + 1);
        record.updateMatchCount(record.getMatchCount() + 1);
        updateTier(memberToken);
        leagueRecordRepository.save(record);

    }

    @Transactional
    public void addDraw(String memberToken) {
        LeagueRecord record = getLeagueRecord(memberToken);
        record.updateDrawCount(record.getDrawCount() + 1);
        record.updateMatchCount(record.getMatchCount() + 1);
        updateTier(memberToken);
        leagueRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public double getWinRate(String memberToken) {
        LeagueRecord record = getLeagueRecord(memberToken);
        if (record.getMatchCount() == 0) {
            return 0.0;
        }
        return (double) record.getWinCount() / record.getMatchCount() * 100;
    }

    private void updateTier(String memberToken) {
        Member member = memberReader.getMember(memberToken);
        double winRate = getWinRate(memberToken);
        LeagueRecord record = getLeagueRecord(memberToken);
        int matchCount = record.getMatchCount();

        if (matchCount >= GOLD_TIER_MIN_MATCHES && winRate >= GOLD_TIER_MIN_WIN_RATE) {
            member.updateTier(Member.MemberTier.GOLD);
        } else if (matchCount >= SILVER_TIER_MIN_MATCHES && winRate >= SILVER_TIER_MIN_WIN_RATE) {
            member.updateTier(Member.MemberTier.SILVER);
        } else {
            member.updateTier(Member.MemberTier.BRONZE);
        }
    }

    public LeagueRecord getLeagueRecord(Member member) {
        return leagueRecordRepository.findByMember(member).orElse(null);
    }

    @Transactional(readOnly = true)
    public LeagueRecord getLeagueRecord(String memberToken) {
        return leagueRecordRepository.findByMemberMemberToken(memberToken).orElse(null);
    }

    private ClubMember getClubMember(Long clubMemberId) {
        return clubMemberRepository.findByClubMemberId(clubMemberId)
                .orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
    }

}
