package org.badminton.domain.domain.club;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubReader {
	Page<Club> readAllClubs(Pageable pageable);

	Club readClub(Long id);
}
