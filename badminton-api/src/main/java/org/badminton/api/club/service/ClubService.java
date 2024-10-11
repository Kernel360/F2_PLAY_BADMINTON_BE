package org.badminton.api.club.service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.badminton.api.club.model.dto.ClubCardResponse;
import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.model.dto.ClubDeleteResponse;
import org.badminton.api.club.model.dto.ClubDetailsResponse;
import org.badminton.api.club.model.dto.ClubUpdateRequest;
import org.badminton.api.club.model.dto.ClubUpdateResponse;
import org.badminton.api.common.exception.club.ClubNameDuplicateException;
import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.member.MemberAlreadyExistInClubException;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.common.exception.member.MemberNotJoinedClubException;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.leaguerecord.repository.LeagueRecordRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
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

	public ClubDetailsResponse readClub(Long clubId) {
		ClubEntity club = checkIfClubPresent(clubId);
		Map<MemberTier, Long> memberCountByTier = club.getClubMemberCountByTier();

		return ClubDetailsResponse.clubEntityToClubReadResponse(club, memberCountByTier);
	}

	public ClubDetailsResponse readCurrentClub(Long memberId) {
		ClubMemberEntity clubMember = findClubMemberByClubMemberId(memberId);
		Map<MemberTier, Long> memberCountByTier = clubMember.getClub().getClubMemberCountByTier();

		return ClubDetailsResponse.clubEntityToClubReadResponse(clubMember.getClub(), memberCountByTier);
	}

	public Page<ClubCardResponse> readAllClubs(Pageable pageable) {
		Page<ClubEntity> clubsPage = clubRepository.findAllByIsClubDeletedIsFalse(pageable);
		return clubsPage.map(club -> {
			Map<MemberTier, Long> tierCounts = club.getClubMemberCountByTier();
			return ClubCardResponse.clubEntityToClubsCardResponse(club, tierCounts);
		});
	}

	public Page<ClubCardResponse> searchClubs(String keyword, Pageable pageable) {
		Page<ClubEntity> clubPage;

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
		ClubEntity club = new ClubEntity(clubAddRequest.clubName(), clubAddRequest.clubDescription(),
			clubAddRequest.clubImage());

		clubRepository.save(club);

		ClubMemberEntity clubMemberEntity = createClubMember(club, memberEntity);

		createLeagueRecord(clubMemberEntity);

		return ClubCreateResponse.clubEntityToClubCreateResponse(club);
	}

	private ClubMemberEntity createClubMember(ClubEntity club, MemberEntity memberEntity) {
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
		ClubEntity club = checkIfClubPresent(clubId);
		club.updateClub(clubUpdateRequest.clubName(), clubUpdateRequest.clubDescription(),
			clubUpdateRequest.clubDescription());
		clubRepository.save(club);
		return ClubUpdateResponse.clubEntityToClubUpdateResponse(club);
	}

	@Transactional
	public ClubDeleteResponse deleteClub(Long clubId) {
		ClubEntity club = checkIfClubPresent(clubId);
		club.doWithdrawal();
		return ClubDeleteResponse.clubEntityToClubDeleteResponse(club);
	}

	public MemberTier getAverageTier(Long clubId) {
		ClubEntity club = checkIfClubPresent(clubId);
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

	private ClubEntity checkIfClubPresent(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(
				() -> new ClubNotExistException(clubId));
	}

	private ClubMemberEntity findClubMemberByClubMemberId(Long memberId) {
		return clubMemberRepository.findByMember_MemberId(memberId)
			.orElseThrow(
				() -> new MemberNotJoinedClubException(memberId));
	}

	private void checkIfMemberAlreadyClubMember(Long memberId) {
		clubMemberRepository.findByMember_MemberId(memberId).ifPresent(clubMember -> {
			if (clubMember.getClub().isClubDeleted()) {
				return;
			}
			throw new MemberAlreadyExistInClubException(memberId, clubMember.getClub().getClubId(),
				clubMember.getRole());
		});
	}

}
