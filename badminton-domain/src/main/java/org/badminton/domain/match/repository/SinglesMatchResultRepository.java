package org.badminton.domain.match.repository;

import org.badminton.domain.match.model.entity.SinglesMatchResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesMatchResultRepository extends JpaRepository<SinglesMatchResultEntity, Long> {
}
