package org.badminton.domain.match.repository;

import org.badminton.domain.match.model.entity.SinglesSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesSetRepository extends JpaRepository<SinglesSetEntity, Long> {
}
