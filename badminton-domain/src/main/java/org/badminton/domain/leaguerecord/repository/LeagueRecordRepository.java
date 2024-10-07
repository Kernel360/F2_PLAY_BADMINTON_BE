package org.badminton.domain.leaguerecord.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeagueRecordRepository extends JpaRepository<LeagueRecordEntity, Long> {
	Optional<LeagueRecordEntity> findByClubMember(ClubMemberEntity clubMember);

	List<LeagueRecordEntity> findTop10ByOrderByWinCountDesc();

	List<LeagueRecordEntity> findAllByTier(MemberTier tier);

	Optional<LeagueRecordEntity> findByClubMember_ClubMemberId(Long clubMemberId);

	@Query("SELECT lr.tier, COUNT(lr) FROM LeagueRecordEntity lr " +
		"JOIN lr.clubMember cm " +
		"JOIN cm.club c " +
		"WHERE c.clubId = :clubId " +
		"GROUP BY lr.tier")
	List<Object[]> countMembersByTierInClub(@Param("clubId") Long clubId);

}
