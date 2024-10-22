package org.badminton.domain.domain.club;

import java.util.Map;

import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.club.info.ClubCardInfo;
import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.badminton.domain.domain.club.info.ClubDeleteInfo;
import org.badminton.domain.domain.club.info.ClubDetailsInfo;
import org.badminton.domain.domain.club.info.ClubUpdateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

	private final ClubReader clubReader;

	// private final ClubStore clubStore;

	@Override
	public Page<ClubCardInfo> readAllClubs(Pageable pageable) {
		Page<Club> clubsPage = clubReader.readAllClubs(pageable);
		return clubsPage.map(club -> {
			Map<MemberTier, Long> tierCounts = club.getClubMemberCountByTier();
			return ClubCardInfo.clubEntityToClubsCardResponse(club, tierCounts);
		});
	}

	@Override
	public ClubDetailsInfo readClub(Long clubId, Long memberId) {
		Club club = clubReader.readClub(clubId);
		Map<MemberTier, Long> memberCountByTier = club.getClubMemberCountByTier();
		boolean isClubMember = checkIfMemberBelongsToClub(memberId, clubId);
		int clubMembersCount = club.getClubMembers().size();
		return ClubDetailsInfo.fromClubEntityAndMemberCountByTier(club, memberCountByTier, isClubMember,
			clubMembersCount);
	}

	@Override
	public boolean checkIfMemberBelongsToClub(Long memberId, Long clubId) {
		return false;
	}

	@Override
	public Page<ClubCardInfo> searchClubs(String keyword, Pageable pageable) {
		return null;
	}

	@Override
	public ClubCreateInfo createClub(ClubCreateCommand clubCreateRequest, Long memberId) {
		return null;
	}

	@Override
	public ClubUpdateInfo updateClub(ClubUpdateCommand clubUpdateRequest, Long clubId) {
		return null;
	}

	@Override
	public ClubDeleteInfo deleteClub(Long clubId) {
		return null;
	}
}
