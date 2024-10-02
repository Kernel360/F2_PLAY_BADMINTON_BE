package org.badminton.api.league.service;

import java.util.List;
import java.util.Objects;

import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.common.exception.league.LeagueParticipationAlreadyCanceledException;
import org.badminton.api.common.exception.league.LeagueParticipationDuplicateException;
import org.badminton.api.common.exception.league.LeagueParticipationNotExistException;
import org.badminton.api.league.model.dto.LeagueParticipantResponse;
import org.badminton.api.league.model.dto.LeagueParticipationCancelResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.league.repository.LeagueParticipantRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueParticipationService {

	private final LeagueParticipantRepository leagueParticipantRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final LeagueRepository leagueRepository;

	public List<LeagueParticipantResponse> findAllLeagueParticipantByLeague(Long leagueId) {
		return leagueParticipantRepository.findAllByLeague_LeagueId(leagueId)
			.stream()
			.map(LeagueParticipantResponse::entityToLeagueParticipantResponse)
			.toList();
	}

	public LeagueParticipantResponse participateInLeague(Long clubId, Long leagueId, Long memberId) {

		ClubMemberEntity clubMember = provideClubMemberIfClubMemberInClub(clubId, memberId);
		LeagueEntity league = provideLeagueIfClubMemberInLeague(clubId, leagueId);

		checkIfClubMemberInLeague(leagueId, clubMember.getClubMemberId());

		LeagueParticipantEntity leagueParticipation = new LeagueParticipantEntity(clubMember, league);
		leagueParticipantRepository.save(leagueParticipation);
		return LeagueParticipantResponse.entityToLeagueParticipantResponse(leagueParticipation);
	}

	public LeagueParticipationCancelResponse cancelLeagueParticipation(Long clubId, Long leagueId, Long memberId) {

		Long clubMemberId = provideClubMemberIfClubMemberInClub(clubId, memberId).getClubMemberId();
		LeagueParticipantEntity leagueParticipant = provideLeagueParticipantIfClubMemberInLeague(leagueId,
			clubMemberId);

		if (leagueParticipant.isCanceled())
			throw new LeagueParticipationAlreadyCanceledException(leagueId, memberId);

		leagueParticipant.cancelLeagueParticipation();
		leagueParticipantRepository.save(leagueParticipant);

		return LeagueParticipationCancelResponse.entityToLeagueParticipationCancelResponse(leagueParticipant);
	}

	private void checkIfClubMemberInLeague(Long leagueId, Long clubMemberId) {
		LeagueParticipantEntity leagueParticipant = leagueParticipantRepository.findByLeague_LeagueIdAndClubMember_ClubMemberId(
			leagueId, clubMemberId).orElse(null);
		if (Objects.isNull(leagueParticipant)) {
			return;
		}
		if (leagueParticipant.isCanceled()) {
			leagueParticipant.reactiveParticipation();
		} else
			throw new LeagueParticipationDuplicateException(leagueId, clubMemberId);

	}

	private LeagueEntity provideLeagueIfClubMemberInLeague(Long clubId, Long leagueId) {
		return leagueRepository.findByClubClubIdAndLeagueId(clubId, leagueId).orElseThrow(
			() -> new LeagueNotExistException(clubId, leagueId));
	}

	private LeagueParticipantEntity provideLeagueParticipantIfClubMemberInLeague(Long leagueId, Long clubMemberId) {
		return leagueParticipantRepository.findByLeague_LeagueIdAndClubMember_ClubMemberId(leagueId, clubMemberId)
			.orElseThrow(
				() -> new LeagueParticipationNotExistException(leagueId, clubMemberId));
	}

	private ClubMemberEntity provideClubMemberIfClubMemberInClub(Long clubId, Long memberId) {
		return clubMemberRepository.findByClub_ClubIdAndMember_MemberId(clubId, memberId).orElseThrow(
			() -> new ClubMemberNotExistException(clubId, memberId));
	}

}
