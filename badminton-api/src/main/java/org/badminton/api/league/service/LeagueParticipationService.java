package org.badminton.api.league.service;

import org.badminton.api.common.exception.league.LeagueParticipationAlreadyCanceledException;
import org.badminton.api.common.exception.league.LeagueParticipationDuplicateException;
import org.badminton.api.common.exception.league.LeagueParticipationNotExistException;
import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.api.league.model.dto.LeagueParticipationCancelResponse;
import org.badminton.api.league.model.dto.LeagueParticipationResponse;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipationEntity;
import org.badminton.domain.league.repository.LeagueParticipateRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeagueParticipationService {

	private final LeagueParticipateRepository leagueParticipateRepository;
	private final MemberRepository memberRepository;
	private final LeagueRepository leagueRepository;

	public LeagueParticipationResponse participateInLeague(Long leagueId, Long memberId) {

		checkIfLeagueParticipationPresent(leagueId, memberId);

		MemberEntity member = memberRepository.findById(memberId).orElseThrow(() ->
			new MemberNotExistException(memberId));

		// TODO: 커스텀 예외 정의
		LeagueEntity league = leagueRepository.findById(leagueId).orElse(null);

		LeagueParticipationEntity leagueParticipation = new LeagueParticipationEntity(member, league);
		leagueParticipateRepository.save(leagueParticipation);
		return LeagueParticipationResponse.entityToLeagueParticipateResponse(leagueParticipation);
	}

	public LeagueParticipationCancelResponse cancelParticipateInLeague(Long leagueId, Long memberId) {

		LeagueParticipationEntity leagueParticipation = provideLeagueParticipation(leagueId, memberId);

		if (leagueParticipation.isCanceled())
			throw new LeagueParticipationAlreadyCanceledException(leagueId, memberId);

		leagueParticipation.cancelLeagueParticipation();
		leagueParticipateRepository.save(leagueParticipation);

		return LeagueParticipationCancelResponse.entityToLeagueParticipateCancelResponse(leagueParticipation);
	}

	private void checkIfLeagueParticipationPresent(Long leagueId, Long memberId) {
		leagueParticipateRepository.findByLeague_LeagueIdAndMember_MemberId(leagueId, memberId).ifPresent(
			participation -> {
				throw new LeagueParticipationDuplicateException(leagueId, memberId);
			}
		);
	}

	private LeagueParticipationEntity provideLeagueParticipation(Long leagueId, Long memberId) {
		return leagueParticipateRepository.findByLeague_LeagueIdAndMember_MemberId(leagueId, memberId).orElseThrow(
			() -> new LeagueParticipationNotExistException(leagueId, memberId));
	}

}
