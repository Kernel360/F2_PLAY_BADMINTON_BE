package org.badminton.api.leaguerecord.service;

import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.leaguerecord.repository.LeagueRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueRecordService {

	private final LeagueRecordRepository leagueRecordRepository;
	private final ClubMemberRepository clubMemberRepository;
	private static final int GOLD_TIER_MIN_MATCHES = 20;
	private static final int GOLD_TIER_MIN_WIN_RATE = 80;
	private static final int SILVER_TIER_MIN_MATCHES = 10;
	private static final int SILVER_TIER_MIN_WIN_RATE = 60;

	@Transactional
	public void addWin(Long clubMemberId) {
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		record.updateWinCount(record.getWinCount() + 1);
		record.updateMatchCount(record.getMatchCount() + 1);
		updateTier(clubMemberId);
		leagueRecordRepository.save(record);
	}

	@Transactional
	public void addLose(Long clubMemberId) {
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		record.updateLoseCount(record.getLoseCount() + 1);
		record.updateMatchCount(record.getMatchCount() + 1);
		updateTier(clubMemberId);
		leagueRecordRepository.save(record);

	}

	@Transactional
	public void addDraw(Long clubMemberId) {
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		record.updateDrawCount(record.getDrawCount() + 1);
		record.updateMatchCount(record.getMatchCount() + 1);
		updateTier(clubMemberId);
		leagueRecordRepository.save(record);
	}

	@Transactional(readOnly = true)
	public double getWinRate(Long clubMemberId) {
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		if (record.getMatchCount() == 0) {
			return 0.0;
		}
		return (double)record.getWinCount() / record.getMatchCount() * 100;
	}

	private void updateTier(Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = getClubMember(clubMemberId);
		double winRate = getWinRate(clubMemberId);
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		int matchCount = record.getMatchCount();

		if (matchCount >= GOLD_TIER_MIN_MATCHES && winRate >= GOLD_TIER_MIN_WIN_RATE) {
			clubMemberEntity.updateTier(MemberTier.GOLD);
			} else if (matchCount >= SILVER_TIER_MIN_MATCHES && winRate >= SILVER_TIER_MIN_WIN_RATE) {
			clubMemberEntity.updateTier(MemberTier.SILVER);
			} else {
			clubMemberEntity.updateTier(MemberTier.BRONZE);
			}
	}

	@Transactional(readOnly = true)
	public LeagueRecordEntity getLeagueRecord(Long clubMemberId) {
		return leagueRecordRepository.findByClubMember_ClubMemberId(clubMemberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
	}

	private ClubMemberEntity getClubMember(Long clubMemberId) {
		return clubMemberRepository.findByClubMemberId(clubMemberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
	}

}
