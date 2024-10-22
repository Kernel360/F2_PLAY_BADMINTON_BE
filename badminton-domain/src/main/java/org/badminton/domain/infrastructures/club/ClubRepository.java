package org.badminton.domain.infrastructures.club;

import java.util.Optional;

import org.badminton.domain.domain.club.entity.ClubEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

    Optional<ClubEntity> findByClubNameAndIsClubDeletedFalse(String clubName);

    Optional<ClubEntity> findByClubIdAndIsClubDeletedFalse(Long clubId);

    Page<ClubEntity> findAllByIsClubDeletedIsFalse(Pageable pageable);

    Page<ClubEntity> findAllByClubNameContainingIgnoreCaseAndIsClubDeletedIsFalse(String keyword, Pageable pageable);
}

