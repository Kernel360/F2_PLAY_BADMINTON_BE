package org.badminton.domain.domain.club;

import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.badminton.domain.domain.club.info.ClubDeleteInfo;
import org.badminton.domain.domain.club.info.ClubDetailsInfo;
import org.badminton.domain.domain.club.info.ClubUpdateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
	Page<ClubCardInfo> readAllClubs(Pageable pageable);

	ClubDetailsInfo readClub(Long clubId, Long memberId);

	boolean checkIfMemberBelongsToClub(Long memberId, Long clubId);

	Page<ClubCardInfo> searchClubs(String keyword, Pageable pageable);

	ClubCreateInfo createClub(ClubCreateCommand clubCreateRequest, Long memberId);

	ClubUpdateInfo updateClub(ClubUpdateCommand clubUpdateRequest, Long clubId);

	ClubDeleteInfo deleteClub(Long clubId);
}
