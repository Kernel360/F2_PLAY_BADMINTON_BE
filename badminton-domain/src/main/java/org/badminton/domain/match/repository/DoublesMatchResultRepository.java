package org.badminton.domain.match.repository;

import org.badminton.domain.match.model.entity.DoublesMatchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoublesMatchResultRepository extends JpaRepository<DoublesMatchResultEntity, Long> {
}
