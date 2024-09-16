package org.badminton.domain.club.repository;

import org.badminton.domain.club.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
}
