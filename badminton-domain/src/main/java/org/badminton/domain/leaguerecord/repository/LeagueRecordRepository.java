package org.badminton.domain.leaguerecord.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRecordRepository extends JpaRepository<LeagueRecordEntity, Long> {
	Optional<LeagueRecordEntity> findByClubMember(ClubMemberEntity clubMember);

	List<LeagueRecordEntity> findTop10ByOrderByWinCountDesc();

	List<LeagueRecordEntity> findAllByTier(MemberTier tier);
}
