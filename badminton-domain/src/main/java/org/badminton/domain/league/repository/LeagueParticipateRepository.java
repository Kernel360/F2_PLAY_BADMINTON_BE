package org.badminton.domain.league.repository;

import java.util.Optional;

import org.badminton.domain.league.entity.LeagueParticipationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueParticipateRepository extends JpaRepository<LeagueParticipationEntity, Long> {

	Optional<LeagueParticipationEntity> findByLeague_LeagueIdAndClubMember_ClubMemberId(Long leagueId,
		Long clubMemberId);

}
