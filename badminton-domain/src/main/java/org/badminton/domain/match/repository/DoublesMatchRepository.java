package org.badminton.domain.match.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoublesMatchRepository extends JpaRepository<DoublesMatchEntity, Long> {

	Optional<DoublesMatchEntity> findByLeague_LeagueId(Long leagueId);

	List<DoublesMatchEntity> findAllByLeague_LeagueId(Long leagueId);
}
