package org.badminton.domain.league.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueParticipantRepository extends JpaRepository<LeagueParticipantEntity, Long> {

	Optional<LeagueParticipantEntity> findByLeague_LeagueIdAndClubMember_ClubMemberId(Long leagueId,
		Long clubMemberId);

	List<LeagueParticipantEntity> findAllByLeague_LeagueId(Long leagueId);

	int countByLeagueLeagueId(Long leagueId);
}
