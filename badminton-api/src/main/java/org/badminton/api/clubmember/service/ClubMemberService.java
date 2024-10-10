package org.badminton.api.clubmember.service;

import java.util.List;

import org.badminton.api.clubmember.model.dto.ClubMemberJoinResponse;
import org.badminton.api.clubmember.model.dto.ClubMemberResponse;
import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.clubmember.ClubMemberDuplicateException;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubMemberService {

	private final ClubMemberRepository clubMemberRepository;
	private final ClubRepository clubRepository;
	private final MemberRepository memberRepository;
	private final LeagueRecordRepository leagueRecordRepository;

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

	public List<ClubMemberEntity> findAllClubMembersByMemberId(Long memberId) {
		return clubMemberRepository.findAllByMember_MemberId(memberId);
	}

	public boolean isMemberOfClub(Long memberId, Long clubId) {
		return clubMemberRepository.existsByMember_MemberIdAndClub_ClubId(memberId, clubId);
	}

	public List<ClubMemberResponse> findAllClubMembers(Long clubId) {
		return clubMemberRepository.findAllByClubClubIdAndBannedFalseAndDeletedFalse(clubId).stream()
			.map(ClubMemberResponse::entityToClubMemberResponse)
			.toList();
	}
}
