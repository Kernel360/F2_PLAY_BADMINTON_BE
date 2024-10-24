package org.badminton.api.application.club;

import java.util.Map;
import java.util.Objects;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.club.ClubService;
import org.badminton.domain.domain.club.command.ClubCreateCommand;
import org.badminton.domain.domain.club.command.ClubUpdateCommand;
import org.badminton.domain.domain.club.info.*;
import org.badminton.domain.domain.clubmember.ClubMemberService;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.league.LeagueRecordService;
import org.badminton.domain.domain.member.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubFacade {
	private final ClubService clubService;
	private final ClubMemberService clubMemberService;
	private final MemberService memberService;
	private final LeagueRecordService leagueRecordService;

	public Page<ClubCardInfo> readAllClubs(int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return clubService.readAllClubs(pageable);
	}

	public ClubDetailsInfo readClub(Long clubId, CustomOAuth2Member clubMember) {
		Long memberId = clubMember.getMemberId();
		var club = clubService.readClub(clubId);
		Map<MemberTier, Long> memberCountByTier = club.getClubMemberCountByTier();
		boolean isClubMember = clubMemberService.checkIfMemberBelongsToClub(memberId, clubId);
		int clubMembersCount = club.clubMembers().size();
		return ClubDetailsInfo.fromClubEntityAndMemberCountByTier(club, memberCountByTier, isClubMember,
			clubMembersCount);
	}

	public Page<ClubCardInfo> searchClubs(String keyword, int page, int size, String sort) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		return clubService.searchClubs(keyword, pageable);
	}

	public ClubCreateInfo createClub(ClubCreateCommand createCommand, Long memberId) {
		clubMemberService.checkMyOwnClub(memberId);
		var clubCreateInfo = clubService.createClub(createCommand);
		var member = memberService.getMemberByMemberId(memberId);
		var clubMember = clubMemberService.createClubMember(clubCreateInfo, member, ClubMemberRole.ROLE_OWNER);
		leagueRecordService.initScore(clubMember);
		return clubCreateInfo;
	}

	public ClubUpdateInfo updateClubInfo(ClubUpdateCommand clubUpdateCommand, Long clubId) {
		return clubService.updateClub(clubUpdateCommand, clubId);
	}

	public ClubDeleteInfo deleteClubInfo(Long clubId) {
		clubMemberService.deleteAllClubMembers(clubId);
		return clubService.deleteClub(clubId);
	}

}
