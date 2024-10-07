package org.badminton.api.leaguerecord.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
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
		double winRate = getWinRate(clubMemberId);
		LeagueRecordEntity record = getLeagueRecord(clubMemberId);
		int matchCount = record.getMatchCount();

		if (matchCount >= 20 && winRate >= 80) {
			record.updateTier(MemberTier.GOLD);
		} else if (matchCount >= 10 && winRate >= 60) {
			record.updateTier(MemberTier.SILVER);
		} else {
			record.updateTier(MemberTier.BRONZE);
		}
	}

	@Transactional(readOnly = true)
	public LeagueRecordEntity getLeagueRecord(Long clubMemberId) {
		return leagueRecordRepository.findByClubMember_ClubMemberId(clubMemberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
	}

	public Map<MemberTier, Long> getMemberCountByTierInClub(Long clubId) {
		List<Object[]> results = leagueRecordRepository.countMembersByTierInClub(clubId);
		Map<MemberTier, Long> tierCounts = new HashMap<>();

		for (Object[] result : results) {
			MemberTier tier = (MemberTier)result[0];
			Long count = (Long)result[1];
			tierCounts.put(tier, count);
		}
		for (MemberTier tier : MemberTier.values()) {
			tierCounts.putIfAbsent(tier, 0L);
		}

		return tierCounts;
	}

}
