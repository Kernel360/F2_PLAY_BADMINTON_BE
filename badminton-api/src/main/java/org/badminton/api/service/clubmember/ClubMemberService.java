package org.badminton.api.service.clubmember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRecordResponse;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRequest;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberExpelRequest;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberJoinResponse;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberResponse;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberRoleUpdateRequest;
import org.badminton.api.interfaces.clubmember.dto.ClubMemberWithdrawResponse;
import org.badminton.domain.common.exception.club.ClubNotExistException;
import org.badminton.domain.common.exception.clubmember.ClubMemberDuplicateException;
import org.badminton.domain.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.league.entity.LeagueRecordEntity;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.badminton.domain.infrastructures.club.ClubRepository;
import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
import org.badminton.domain.infrastructures.league.LeagueRecordRepository;
import org.badminton.domain.infrastructures.member.MemberRepository;
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
		Club club = clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(() -> new ClubNotExistException(clubId));

		MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));

		if (clubMemberRepository.existsByMember_MemberIdAndDeletedFalse(memberId)) {
			throw new ClubMemberDuplicateException(clubId, memberId);
		}

		ClubMemberEntity clubMemberEntity = new ClubMemberEntity(club, memberEntity, ClubMemberRole.ROLE_USER);

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

	public ClubMemberBanRecordResponse expelClubMember(ClubMemberExpelRequest request, Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = getClubMember(clubMemberId);
		return expelStrategy.execute(clubMemberEntity, request);
	}

	public ClubMemberBanRecordResponse banClubMember(ClubMemberBanRequest request, Long clubMemberId) {
		ClubMemberEntity clubMemberEntity = getClubMember(clubMemberId);
		return banStrategy.execute(clubMemberEntity, request);
	}

	public ClubMemberWithdrawResponse withDrawClubMember(Long clubId, Long memberId) {
		ClubMemberEntity clubMember = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
			.orElseThrow(() -> new ClubMemberNotExistException(clubId, memberId));
		clubMember.withdrawal();
		clubMemberRepository.save(clubMember);
		return new ClubMemberWithdrawResponse(clubId, clubMember.getClubMemberId(), clubMember.isDeleted());
	}

}
