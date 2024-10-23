package org.badminton.domain.domain.club;

import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.badminton.domain.domain.club.info.ClubDeleteInfo;
import org.badminton.domain.domain.club.info.ClubUpdateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
	Page<ClubCardInfo> readAllClubs(Pageable pageable);

	Club readClub(Long clubId, Long memberId);

	Page<ClubCardInfo> searchClubs(String keyword, Pageable pageable);

	Club createClub(ClubCreateCommand clubCreateCommand);

	ClubUpdateInfo updateClub(ClubUpdateCommand clubUpdateCommand, Long clubId);

	ClubDeleteInfo deleteClub(Long clubId);
}
