package org.badminton.domain.infrastructures.league;

import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.league.LeagueRecordStore;
import org.badminton.domain.domain.league.entity.LeagueRecordEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeagueRecordStoreImpl implements LeagueRecordStore {
	private final LeagueRecordRepository leagueRecordRepository;

	@Override
	public void initScore(ClubMemberEntity clubMember) {
		LeagueRecordEntity leagueRecord = new LeagueRecordEntity(clubMember);
		leagueRecordRepository.save(leagueRecord);
	}
}
