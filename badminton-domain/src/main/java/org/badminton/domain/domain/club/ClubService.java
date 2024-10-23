package org.badminton.domain.domain.club;

import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.club.info.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
	Page<ClubCardInfo> readAllClubs(Pageable pageable);

	ClubSummaryInfo readClub(Long clubId );

	Page<ClubCardInfo> searchClubs(String keyword, Pageable pageable);

	ClubCreateInfo createClub(ClubCreateCommand clubCreateCommand);

	ClubUpdateInfo updateClub(ClubUpdateCommand clubUpdateCommand, Long clubId);

	ClubDeleteInfo deleteClub(Long clubId);
}
