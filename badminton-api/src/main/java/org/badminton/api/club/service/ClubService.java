package org.badminton.api.club.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.badminton.api.club.model.dto.ClubCreateRequest;
import org.badminton.api.club.model.dto.ClubCreateResponse;
import org.badminton.api.club.model.dto.ClubDeleteResponse;
import org.badminton.api.club.model.dto.ClubReadResponse;
import org.badminton.api.club.model.dto.ClubUpdateRequest;
import org.badminton.api.club.model.dto.ClubUpdateResponse;
import org.badminton.api.club.model.dto.ClubsReadResponse;
import org.badminton.api.common.exception.club.ClubNameDuplicateException;
import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.common.exception.member.MemberNotJoinedClubException;
import org.badminton.api.leaguerecord.service.LeagueRecordService;
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
	private final LeagueRecordService leagueRecordService;

	public ClubReadResponse readClub(Long clubId) {
		ClubEntity club = findClubByClubId(clubId);
		return ClubReadResponse.clubEntityToClubReadResponse(club);
	}

	public ClubReadResponse readCurrentClub(Long memberId) {
		ClubMemberEntity clubMember = findClubMemberByClubMemberId(memberId);
		return ClubReadResponse.clubEntityToClubReadResponse(clubMember.getClub());
	}

	public List<ClubsReadResponse> readAllClub() {
		List<ClubEntity> clubs = clubRepository.findAllByIsClubDeletedIsFalse();

		return clubs.stream()
			.map(club -> {
				Map<MemberTier, Long> tierCounts = leagueRecordService.getMemberCountByTierInClub(club.getClubId());
				return ClubsReadResponse.clubEntityToClubsReadResponse(club, tierCounts);
			})
			.collect(Collectors.toList());
	}

	// TODO: clubAddRequest에 이미지가 없으면 default 이미지를 넣어주도록 구현
	@Transactional
	public ClubCreateResponse createClub(ClubCreateRequest clubAddRequest, Long memberId) {

		MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));

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
		ClubEntity club = findClubByClubId(clubId);
		club.updateClub(clubUpdateRequest.clubName(), clubUpdateRequest.clubDescription(),
			clubUpdateRequest.clubDescription());
		clubRepository.save(club);
		return ClubUpdateResponse.clubEntityToClubUpdateResponse(club);
	}

	@Transactional
	public ClubDeleteResponse deleteClub(Long clubId) {
		ClubEntity club = findClubByClubId(clubId);
		club.doWithdrawal();
		return ClubDeleteResponse.clubEntityToClubDeleteResponse(club);
	}

	public MemberTier getAverageTier(Long clubId) {
		Map<MemberTier, Long> memberCountByTierInClub = leagueRecordService.getMemberCountByTierInClub(clubId);

		Optional<Map.Entry<MemberTier, Long>> maxEntry = memberCountByTierInClub.entrySet()
			.stream()
			.max(Map.Entry.comparingByValue());

		if (maxEntry.isPresent() && maxEntry.get().getValue() > 0) {
			return maxEntry.get().getKey();
		} else {
			return MemberTier.SILVER;
		}
	}

	public List<ClubsReadResponse> searchClubs(String keyword) {

		if (keyword == null || keyword.trim().isEmpty()) {
			return readAllClub();
		}

		List<ClubEntity> clubEntityList = clubRepository.findAllByClubNameContainingIgnoreCase(
			keyword);

		return clubEntityList.stream()
			.map(club -> {
				Map<MemberTier, Long> tierCounts = leagueRecordService.getMemberCountByTierInClub(club.getClubId());
				return ClubsReadResponse.clubEntityToClubsReadResponse(club, tierCounts);
			})
			.collect(Collectors.toList());

	}

	private void checkClubNameDuplicate(String clubName) {
		clubRepository.findByClubNameAndIsClubDeletedFalse(clubName).ifPresent(club -> {
			throw new ClubNameDuplicateException(clubName);
		});
	}

	private ClubEntity findClubByClubId(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(
				() -> new ClubNotExistException(clubId));
	}

	private ClubMemberEntity findClubMemberByClubMemberId(Long memberId) {
		return clubMemberRepository.findByMember_MemberId(memberId)
            .orElseThrow(
                () -> new MemberNotJoinedClubException(memberId));
	}

}
