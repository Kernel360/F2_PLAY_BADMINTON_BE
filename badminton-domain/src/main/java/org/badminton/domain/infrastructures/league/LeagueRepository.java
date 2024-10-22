package org.badminton.domain.infrastructures.league;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.badminton.domain.domain.league.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {

    Optional<LeagueEntity> findByClubClubIdAndLeagueId(Long clubId, Long leagueId);

    List<LeagueEntity> findAllByClubClubIdAndLeagueAtBetween(Long clubId, LocalDateTime startOfMonth,
                                                             LocalDateTime endOfMonth);

    void deleteByLeagueId(Long leagueId);
}
