package org.badminton.domain.domain.clubmember.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.badminton.domain.common.exception.club.ClubNotExistException;
import org.badminton.domain.common.exception.clubmember.ClubMemberDuplicateException;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.club.info.ClubCreateInfo;
import org.badminton.domain.domain.clubmember.ClubMemberReader;
import org.badminton.domain.domain.clubmember.ClubMemberStore;
import org.badminton.domain.domain.clubmember.command.ClubMemberBanCommand;
import org.badminton.domain.domain.clubmember.command.ClubMemberExpelCommand;
import org.badminton.domain.domain.clubmember.command.ClubMemberRoleUpdateCommand;
import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.badminton.domain.domain.clubmember.info.ClubMemberBanRecordInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberJoinInfo;
import org.badminton.domain.domain.clubmember.info.ClubMemberWithdrawInfo;
import org.badminton.domain.domain.member.MemberReader;
import org.badminton.domain.domain.member.entity.Member;
import org.badminton.domain.infrastructures.club.ClubRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClubMemberServiceImpl implements ClubMemberService {

	private final ClubRepository clubRepository;
	private final MemberReader memberReader;
	private final ClubMemberReader clubMemberReader;
	private final ClubMemberStore clubMemberStore;
	private final ClubMemberPenaltyStrategy expelStrategy;
	private final ClubMemberPenaltyStrategy banStrategy;

	public ClubMemberServiceImpl(ClubRepository clubRepository, MemberReader memberReader,
		ClubMemberReader clubMemberReader, ClubMemberStore clubMemberStore) {
		this.clubRepository = clubRepository;
		this.memberReader = memberReader;
		this.clubMemberReader = clubMemberReader;
		this.clubMemberStore = clubMemberStore;
		this.expelStrategy = new ExpelStrategy(clubMemberStore);
		this.banStrategy = new BanStrategy(clubMemberStore);
	}

	@Override
	public ClubMemberJoinInfo joinClub(
		String memberToken, Long clubId) {

		Club club = clubRepository.findByClubIdAndIsClubDeletedFalse(clubId)
			.orElseThrow(() -> new ClubNotExistException(clubId));

		Member member = memberReader.getMember(memberToken);

		if (clubMemberReader.isExist(memberToken)) {
			throw new ClubMemberDuplicateException(clubId, memberToken);
		}

		ClubMember clubMember = new ClubMember(club, member, ClubMember.ClubMemberRole.ROLE_USER);

		clubMemberStore.store(clubMember);

		return ClubMemberJoinInfo.from(clubMember);
	}

	@Override
	public void createClubJoinInfo(String memberToken, ClubCreateInfo clubInfo) {
		Member member = memberReader.getMember(memberToken);
		var club = new Club(clubInfo);
		ClubMember clubMember = new ClubMember(club, member, ClubMember.ClubMemberRole.ROLE_OWNER);
		clubMemberStore.store(clubMember);
	}

	@Override
	public ClubMemberInfo updateClubMemberRole(ClubMemberRoleUpdateCommand request, Long clubMemberId) {
		ClubMember clubMember = clubMemberReader.getClubMember(clubMemberId);
		clubMember.updateClubMemberRole(request.role());
		clubMemberStore.store(clubMember);
		return ClubMemberInfo.valueOf(clubMember);
	}

	@Override
	public Map<ClubMember.ClubMemberRole, List<ClubMemberInfo>> findAllClubMembers(Long clubId) {
		Map<ClubMember.ClubMemberRole, List<ClubMemberInfo>> responseMap = new TreeMap<>(
			new ClubMemberRoleComparator());

		List<ClubMember> clubMembers =
			clubMemberReader.getAllClubMemberByClubId(clubId);

		clubMembers.stream()
			.map(ClubMemberInfo::valueOf) // 멤버를 응답 객체로 변환
			.forEach(clubMemberResponse -> {
				ClubMember.ClubMemberRole role = clubMemberResponse.role();
				responseMap.computeIfAbsent(role, clubMember -> new ArrayList<>()).add(clubMemberResponse);
			});

		return responseMap;
	}

	@Override
	public ClubMember getClubMember(Long clubMemberId) {
		return clubMemberReader.getClubMember(clubMemberId);
	}

	@Override
	public ClubMemberBanRecordInfo expelClubMember(ClubMemberExpelCommand request, Long clubMemberId) {
		ClubMember clubMember = getClubMember(clubMemberId);
		return expelStrategy.execute(clubMember, request);
	}

	@Override
	public ClubMemberBanRecordInfo banClubMember(ClubMemberBanCommand request, Long clubMemberId) {
		ClubMember clubMember = getClubMember(clubMemberId);
		return banStrategy.execute(clubMember, request);
	}

	@Override
	public ClubMemberWithdrawInfo withDrawClubMember(Long clubId, String memberToken) {
		ClubMember clubMember = clubMemberReader.getClubMember(clubId);
		clubMember.withdrawal();
		clubMemberStore.store(clubMember);
		return new ClubMemberWithdrawInfo(clubId, clubMember.getClubMemberId(), clubMember.isDeleted());
	}

	@Override
	public boolean checkIfMemberBelongsToClub(String memberToken, Long clubId) {
		return clubMemberReader.checkIsClubMember(memberToken, clubId);
	}

	@Override
	public void checkMyOwnClub(String memberToken) {
		clubMemberReader.checkIsClubOwner(memberToken);
	}

	@Override
	public void deleteAllClubMembers(Long clubId) {
		List<ClubMember> clubMembers = clubMemberReader.getAllMember(clubId);
		clubMembers.forEach(clubMember -> {
			clubMember.deleteClubMember();
			clubMemberStore.store(clubMember);
		});
	}

	@Override
	public ClubMember getClubMember(String memberToken) {
		return clubMemberReader.getClubMemberByMemberToken(memberToken);
	}
}
