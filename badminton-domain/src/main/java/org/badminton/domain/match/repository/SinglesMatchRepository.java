package org.badminton.domain.match.repository;

import java.util.List;

import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesMatchRepository extends JpaRepository<SinglesMatchEntity, Long> {

	List<SinglesMatchEntity> findAllByLeague_LeagueId(Long leagueId);

}
