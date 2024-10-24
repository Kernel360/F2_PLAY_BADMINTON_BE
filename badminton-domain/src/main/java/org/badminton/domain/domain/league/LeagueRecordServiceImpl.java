package org.badminton.domain.domain.league;

import org.badminton.domain.domain.member.entity.Member;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueRecordServiceImpl implements LeagueRecordService {
	private final LeagueRecordStore leagueRecordStore;

	@Override
	public void initScore(Member member) {
		leagueRecordStore.initScore(member);
	}
}
