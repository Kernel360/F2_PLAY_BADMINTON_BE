package org.badminton.domain.match.repository;

import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoublesMatchRepository extends JpaRepository<DoublesMatchEntity, Long> {
}
