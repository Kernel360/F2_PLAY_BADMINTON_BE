package org.badminton.domain.match.repository;

import java.util.Optional;

import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesMatchRepository extends JpaRepository<SinglesMatchEntity, Long> {

	Optional<SinglesMatchEntity> findAllByLeague_LeagueId(Long leagueId);

}
