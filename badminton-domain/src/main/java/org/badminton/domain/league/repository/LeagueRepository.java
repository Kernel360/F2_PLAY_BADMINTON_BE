package org.badminton.domain.league.repository;

import org.badminton.domain.league.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
}
