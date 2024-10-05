package org.badminton.domain.match.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesMatchRepository extends JpaRepository<SinglesMatchEntity, Long> {

	Optional<SinglesMatchEntity> findByLeague_LeagueId(Long leagueId);

	List<SinglesMatchEntity> findAllByLeague_LeagueId(Long leagueId);

}
