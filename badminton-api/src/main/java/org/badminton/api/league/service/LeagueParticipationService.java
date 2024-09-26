package org.badminton.api.league.service;

import java.util.Objects;

import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.common.exception.league.LeagueParticipationAlreadyCanceledException;
import org.badminton.api.common.exception.league.LeagueParticipationDuplicateException;
import org.badminton.api.common.exception.league.LeagueParticipationNotExistException;
import org.badminton.api.league.model.dto.LeagueParticipationCancelResponse;
import org.badminton.api.league.model.dto.LeagueParticipationResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipationEntity;
import org.badminton.domain.league.repository.LeagueParticipateRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueParticipationService {

	private final LeagueParticipateRepository leagueParticipateRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final LeagueRepository leagueRepository;

	public LeagueParticipationResponse participateInLeague(Long clubId, Long leagueId, Long memberId) {

		ClubMemberEntity clubMember = provideClubMemberIfClubMemberInClub(clubId, memberId);
		LeagueEntity league = provideLeagueIfClubMemberInLeague(clubId, leagueId);

		checkIfClubMemberInLeague(leagueId, clubMember.getClubMemberId());

		LeagueParticipationEntity leagueParticipation = new LeagueParticipationEntity(clubMember, league);
		leagueParticipateRepository.save(leagueParticipation);
		return LeagueParticipationResponse.entityToLeagueParticipateResponse(leagueParticipation);
	}

	public LeagueParticipationCancelResponse cancelLeagueParticipation(Long clubId, Long leagueId, Long memberId) {

		Long clubMemberId = provideClubMemberIfClubMemberInClub(clubId, memberId).getClubMemberId();
		LeagueParticipationEntity leagueParticipation = provideLeagueParticipationIfClubMemberInLeague(leagueId,
			clubMemberId);

		if (leagueParticipation.isCanceled())
			throw new LeagueParticipationAlreadyCanceledException(leagueId, memberId);

		leagueParticipation.cancelLeagueParticipation();
		leagueParticipateRepository.save(leagueParticipation);

		return LeagueParticipationCancelResponse.entityToLeagueParticipateCancelResponse(leagueParticipation);
	}

	private void checkIfClubMemberInLeague(Long leagueId, Long clubMemberId) {
		LeagueParticipationEntity leagueParticipation = leagueParticipateRepository.findByLeague_LeagueIdAndClubMember_ClubMemberId(
			leagueId, clubMemberId).orElse(null);
		if (Objects.nonNull(leagueParticipation) && leagueParticipation.isCanceled()) {
			leagueParticipation.reactiveParticipation();
		} else
			throw new LeagueParticipationDuplicateException(leagueId, clubMemberId);

	}

	private LeagueEntity provideLeagueIfClubMemberInLeague(Long clubId, Long leagueId) {
		return leagueRepository.findByClubClubIdAndLeagueId(clubId, leagueId).orElseThrow(
			() -> new LeagueNotExistException(clubId, leagueId));
	}

	private LeagueParticipationEntity provideLeagueParticipationIfClubMemberInLeague(Long leagueId, Long clubMemberId) {
		return leagueParticipateRepository.findByLeague_LeagueIdAndClubMember_ClubMemberId(leagueId, clubMemberId)
			.orElseThrow(
				() -> new LeagueParticipationNotExistException(leagueId, clubMemberId));
	}

	private ClubMemberEntity provideClubMemberIfClubMemberInClub(Long clubId, Long memberId) {
		return clubMemberRepository.findByClub_ClubIdAndMember_MemberId(clubId, memberId).orElseThrow(
			() -> new ClubMemberNotExistException(clubId, memberId));
	}

}
