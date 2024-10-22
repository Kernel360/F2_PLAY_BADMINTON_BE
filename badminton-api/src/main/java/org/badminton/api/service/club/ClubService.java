package org.badminton.api.service.club;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.badminton.api.common.exception.member.MemberAlreadyExistInClubException;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.common.exception.member.MemberNotJoinedClubException;
import org.badminton.api.interfaces.club.dto.ClubCardResponse;
import org.badminton.api.interfaces.club.dto.ClubCreateRequest;
import org.badminton.api.interfaces.club.dto.ClubCreateResponse;
import org.badminton.api.interfaces.club.dto.ClubDeleteResponse;
import org.badminton.api.interfaces.club.dto.ClubDetailsResponse;
import org.badminton.api.interfaces.club.dto.ClubUpdateRequest;
import org.badminton.api.interfaces.club.dto.ClubUpdateResponse;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.common.exception.club.ClubNameDuplicateException;
import org.badminton.domain.common.exception.club.ClubNotExistException;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.league.entity.LeagueRecordEntity;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.badminton.domain.infrastructures.club.ClubRepository;
import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
import org.badminton.domain.infrastructures.league.LeagueRecordRepository;
import org.badminton.domain.infrastructures.member.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {

	private final ClubRepository clubRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final MemberRepository memberRepository;
	private final LeagueRecordRepository leagueRecordRepository;

	public ClubDetailsResponse readClub(Long clubId, Long memberId) {
		Club club = checkIfClubPresent(clubId);
		Map<MemberTier, Long> memberCountByTier = club.getClubMemberCountByTier();

		boolean isClubMember = checkIfMemberBelongsToClub(memberId, clubId);

		int clubMembersCount = clubMemberRepository.findAllByDeletedFalseAndClub_ClubId(
			clubId).size();

		return ClubDetailsResponse.fromClubEntityAndMemberCountByTier(club, memberCountByTier, isClubMember,
			clubMembersCount);
	}

	private boolean checkIfMemberBelongsToClub(Long memberId, Long clubId) {
		if (Objects.isNull(memberId)) {
			return false;
		}
		return clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
			.map(clubMember -> Objects.equals(clubMember.getClub().getClubId(), clubId))
			.orElse(false);
	}

	public Page<ClubCardResponse> readAllClubs(Pageable pageable) {
		Page<Club> clubsPage = clubRepository.findAllByIsClubDeletedIsFalse(pageable);
		return clubsPage.map(club -> {
			Map<MemberTier, Long> tierCounts = club.getClubMemberCountByTier();
			return ClubCardResponse.clubEntityToClubsCardResponse(club, tierCounts);
		});
	}

	public Page<ClubCardResponse> searchClubs(String keyword, Pageable pageable) {
		Page<Club> clubPage;

		if (Objects.isNull(keyword) || keyword.trim().isEmpty()) {
			clubPage = clubRepository.findAllByIsClubDeletedIsFalse(pageable);
		} else {
			clubPage = clubRepository.findAllByClubNameContainingIgnoreCaseAndIsClubDeletedIsFalse(keyword, pageable);
		}
		return clubPage.map(club -> {
			Map<MemberTier, Long> tierCounts = club.getClubMemberCountByTier();
			return ClubCardResponse.clubEntityToClubsCardResponse(club, tierCounts);
		});
	}

	// TODO: clubAddRequest에 이미지가 없으면 default 이미지를 넣어주도록 구현
	@Transactional
	public ClubCreateResponse createClub(ClubCreateRequest clubAddRequest, Long memberId) {

		MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));

		checkIfMemberAlreadyClubMember(memberId);
		checkClubNameDuplicate(clubAddRequest.clubName());
		Club club = new Club(clubAddRequest.clubName(), clubAddRequest.clubDescription(),
			clubAddRequest.clubImage());

		clubRepository.save(club);

		ClubMemberEntity clubMemberEntity = createClubMember(club, memberEntity);

		createLeagueRecord(clubMemberEntity);

		return ClubCreateResponse.fromClubEntity(club);
	}

	private ClubMemberEntity createClubMember(Club club, MemberEntity memberEntity) {
		ClubMemberEntity clubMemberEntity = new ClubMemberEntity(club, memberEntity, ClubMemberRole.ROLE_OWNER);
		clubMemberRepository.save(clubMemberEntity);
		return clubMemberEntity;
	}

	private void createLeagueRecord(ClubMemberEntity clubMemberEntity) {
		LeagueRecordEntity leagueRecord = new LeagueRecordEntity(clubMemberEntity);
		leagueRecordRepository.save(leagueRecord);
	}

	@Transactional
	public ClubUpdateResponse updateClub(ClubUpdateRequest clubUpdateRequest, Long clubId) {
		Club club = checkIfClubPresent(clubId);
		club.updateClub(clubUpdateRequest.clubName(), clubUpdateRequest.clubDescription(),
			clubUpdateRequest.clubImage());
		clubRepository.save(club);
		return ClubUpdateResponse.fromClubEntity(club);
	}

	@Transactional
	public ClubDeleteResponse deleteClub(Long clubId) {
		Club club = checkIfClubPresent(clubId);
		club.doWithdrawal();
		deleteAllClubMembers(clubId);
		return ClubDeleteResponse.clubEntityToClubDeleteResponse(club);
	}

	public void deleteAllClubMembers(Long clubId) {
		List<ClubMemberEntity> clubMembers = clubMemberRepository.findAllByClub_ClubId(clubId);
		clubMembers.forEach(clubMember -> {
			clubMember.deleteClubMember();
			clubMemberRepository.save(clubMember);
		});
	}

	public MemberTier getAverageTier(Long clubId) {
		Club club = checkIfClubPresent(clubId);
		Map<MemberTier, Long> memberCountByTierInClub = club.getClubMemberCountByTier();

		Optional<Map.Entry<MemberTier, Long>> maxEntry = memberCountByTierInClub.entrySet()
			.stream()
			.max(Map.Entry.comparingByValue());

		if (maxEntry.isPresent() && maxEntry.get().getValue() > 0) {
			return maxEntry.get().getKey();
		} else {
			return MemberTier.SILVER;
		}
	}

	private void checkClubNameDuplicate(String clubName) {
		clubRepository.findByClubNameAndIsClubDeletedFalse(clubName).ifPresent(club -> {
			throw new ClubNameDuplicateException(clubName);
		});
	}

	private Club checkIfClubPresent(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(
				() -> new ClubNotExistException(clubId));
	}

	private ClubMemberEntity findClubMemberByClubMemberId(Long memberId) {
		return clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
			.orElseThrow(
				() -> new MemberNotJoinedClubException(memberId));
	}

	private void checkIfMemberAlreadyClubMember(Long memberId) {
		clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId).ifPresent(clubMember -> {
			throw new MemberAlreadyExistInClubException(memberId, clubMember.getClub().getClubId(),
				clubMember.getRole());
		});
	}
}
