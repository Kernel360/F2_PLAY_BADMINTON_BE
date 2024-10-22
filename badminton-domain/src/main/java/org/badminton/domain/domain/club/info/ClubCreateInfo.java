package org.badminton.domain.domain.club.info;

import java.util.List;

import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.league.entity.LeagueRecordEntity;
import org.badminton.domain.domain.member.entity.MemberEntity;

public record ClubCreateInfo(
	Long id,
	boolean deleted,
	boolean banned,
	ClubMemberRole role,
	MemberTier tier,
	Club club,
	MemberEntity member,
	LeagueRecordEntity leagueRecord,
	List<ClubMemberBanRecordEntity> banHistory
) {

}
