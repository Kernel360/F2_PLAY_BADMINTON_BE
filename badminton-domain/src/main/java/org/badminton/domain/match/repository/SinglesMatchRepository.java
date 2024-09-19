package org.badminton.domain.match.repository;

import org.badminton.domain.match.entity.SinglesMatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SinglesMatchRepository extends JpaRepository<SinglesMatchEntity, Long> {

}
