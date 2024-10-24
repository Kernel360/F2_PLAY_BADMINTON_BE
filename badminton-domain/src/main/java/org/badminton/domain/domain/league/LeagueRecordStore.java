package org.badminton.domain.domain.league;

import org.badminton.domain.domain.member.entity.Member;

public interface LeagueRecordStore {
	void initScore(Member member);
}
