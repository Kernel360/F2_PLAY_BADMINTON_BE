package org.badminton.domain.infrastructures.club;

import java.util.Optional;

import org.badminton.domain.domain.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {

	Optional<Club> findByClubNameAndIsClubDeletedFalse(String clubName);

	Optional<Club> findByClubIdAndIsClubDeletedFalse(Long clubId);

	Page<Club> findAllByIsClubDeletedIsFalse(Pageable pageable);

	Page<Club> findAllByClubNameContainingIgnoreCaseAndIsClubDeletedIsFalse(String keyword, Pageable pageable);
}

