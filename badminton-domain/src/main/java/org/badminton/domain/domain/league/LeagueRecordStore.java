package org.badminton.domain.domain.league;

import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public interface LeagueRecordStore {
	void initScore(ClubMemberEntity clubMember);
}
