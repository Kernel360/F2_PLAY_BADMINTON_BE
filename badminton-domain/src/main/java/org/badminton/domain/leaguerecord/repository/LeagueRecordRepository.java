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

	@Query("SELECT leagueEntity.tier, COUNT(leagueEntity) FROM LeagueRecordEntity leagueEntity " +
		"JOIN leagueEntity.clubMember clubMemberEntity " +
		"JOIN clubMemberEntity.club clubEntity " +
		"WHERE clubEntity.clubId = :clubId " +
		"GROUP BY leagueEntity.tier")
	List<Object[]> countMembersByTierInClub(@Param("clubId") Long clubId);

}
