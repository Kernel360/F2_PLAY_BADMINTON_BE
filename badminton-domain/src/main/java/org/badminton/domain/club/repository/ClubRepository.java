package org.badminton.domain.club.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.club.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {

	Optional<ClubEntity> findByClubNameAndIsClubDeletedFalse(String clubName);

	Optional<ClubEntity> findByClubIdAndIsClubDeletedFalse(Long clubId);

	List<ClubEntity> findAllByIsClubDeletedIsFalse();

	List<ClubEntity> findAllByClubNameContainingIgnoreCase(String keyword);
}

