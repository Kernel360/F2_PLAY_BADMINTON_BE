package org.badminton.api.clubmember.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.badminton.api.clubmember.BanStrategy;
import org.badminton.api.clubmember.ClubMemberPenaltyStrategy;
import org.badminton.api.clubmember.ExpelStrategy;
import org.badminton.api.clubmember.model.Comparator.ClubMemberRoleComparator;
import org.badminton.api.clubmember.model.dto.clubMemberBanRecordResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberBanRequest;
import org.badminton.api.clubmember.model.dto.ClubMemberExpelRequest;
import org.badminton.api.clubmember.model.dto.ClubMemberJoinResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberRoleUpdateRequest;
import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.clubmember.ClubMemberDuplicateException;
import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.leaguerecord.entity.LeagueRecordEntity;
import org.badminton.domain.leaguerecord.repository.LeagueRecordRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ClubMemberService {

	private final ClubMemberRepository clubMemberRepository;
	private final ClubRepository clubRepository;
	private final MemberRepository memberRepository;
	private final LeagueRecordRepository leagueRecordRepository;
	private final ClubMemberPenaltyStrategy expelStrategy;
	private final ClubMemberPenaltyStrategy banStrategy;

	public ClubMemberService(ClubMemberRepository clubMemberRepository, ClubRepository clubRepository,
		MemberRepository memberRepository, LeagueRecordRepository leagueRecordRepository) {
		this.clubMemberRepository = clubMemberRepository;
		this.clubRepository = clubRepository;
		this.memberRepository = memberRepository;
		this.leagueRecordRepository = leagueRecordRepository;
		this.expelStrategy = new ExpelStrategy(clubMemberRepository);
		this.banStrategy = new BanStrategy(clubMemberRepository);
	}

	public ClubMemberJoinResponse joinClub(Long memberId, Long clubId) {
		ClubEntity clubEntity = clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(() -> new ClubNotExistException(clubId));

		MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));

		if (clubMemberRepository.existsByMember_MemberId(memberId)) {
			throw new ClubMemberDuplicateException(clubId, memberId);
		}

		ClubMemberEntity clubMemberEntity = new ClubMemberEntity(clubEntity, memberEntity, ClubMemberRole.ROLE_USER);

		clubMemberRepository.save(clubMemberEntity);

		LeagueRecordEntity leagueRecord = new LeagueRecordEntity(clubMemberEntity);
		leagueRecordRepository.save(leagueRecord);

		return ClubMemberJoinResponse.clubMemberEntityToClubMemberJoinResponse(
			clubMemberEntity);

	}

	public ClubMemberResponse updateClubMemberRole(ClubMemberRoleUpdateRequest request, Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = clubMemberRepository.findByClubMemberId(clubMemberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
		clubMemberEntity.updateClubMemberRole(request.role());
		clubMemberRepository.save(clubMemberEntity);
		return ClubMemberResponse.entityToClubMemberResponse(clubMemberEntity);
	}

	public List<ClubMemberEntity> findAllClubMembersByMemberId(Long memberId) {
		return clubMemberRepository.findAllByMember_MemberId(memberId);
	}

	public boolean isMemberOfClub(Long memberId, Long clubId) {
		return clubMemberRepository.existsByMember_MemberIdAndClub_ClubId(memberId, clubId);
	}

	public Map<ClubMemberRole, List<ClubMemberResponse>> findAllClubMembers(Long clubId) {
		Map<ClubMemberRole, List<ClubMemberResponse>> responseMap = new TreeMap<>(new ClubMemberRoleComparator());

		List<ClubMemberEntity> clubMembers =
			clubMemberRepository.findAllByClubClubIdAndBannedFalseAndDeletedFalse(clubId);

		clubMembers.stream()
			.map(ClubMemberResponse::entityToClubMemberResponse) // 멤버를 응답 객체로 변환
			.forEach(clubMemberResponse -> {
				ClubMemberRole role = clubMemberResponse.role();
				responseMap.computeIfAbsent(role, clubMember -> new ArrayList<>()).add(clubMemberResponse);
			});

		return responseMap;
	}

	private ClubMemberEntity getClubMember(Long clubMemberId) {
		return clubMemberRepository.findByClubMemberId(clubMemberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubMemberId));
	}

	public clubMemberBanRecordResponse expelClubMember(ClubMemberExpelRequest request, Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = getClubMember(clubMemberId);
		return expelStrategy.execute(clubMemberEntity, request);
	}

	public clubMemberBanRecordResponse banClubMember(ClubMemberBanRequest request, Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = getClubMember(clubMemberId);
		return banStrategy.execute(clubMemberEntity, request);
	}
}
