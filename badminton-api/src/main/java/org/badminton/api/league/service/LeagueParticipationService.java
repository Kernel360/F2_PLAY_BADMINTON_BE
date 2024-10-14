package org.badminton.api.league.service;

import java.util.List;
import java.util.Objects;

import org.badminton.api.common.exception.clubmember.ClubMemberNotExistException;
import org.badminton.api.common.exception.league.InsufficientTierException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.common.exception.league.LeagueParticipationAlreadyCanceledException;
import org.badminton.api.common.exception.league.LeagueParticipationDuplicateException;
import org.badminton.api.common.exception.league.LeagueParticipationNotExistException;
import org.badminton.api.common.exception.league.LeagueRecruitingCompletedException;
import org.badminton.api.league.model.dto.LeagueParticipantResponse;
import org.badminton.api.league.model.dto.LeagueParticipationCancelResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;
import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.entity.LeagueParticipantEntity;
import org.badminton.domain.league.enums.LeagueStatus;
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
		MemberTier requiredTier = league.getRequiredTier();
		MemberTier clubMemberTier = clubMember.getTier();
		checkLeagueRecruitingStatus(league);
		checkIfClubMemberInLeague(leagueId, clubMember.getClubMemberId());
		switch (requiredTier) {
			case GOLD -> checkGoldMemberTier(clubMemberTier, leagueId, clubId);
			case SILVER -> checkSilverMemberTIer(clubMemberTier, leagueId, clubId);
		}

		LeagueParticipantEntity leagueParticipation = new LeagueParticipantEntity(clubMember, league);
		leagueParticipantRepository.save(leagueParticipation);

		checkPlayerCount(league);
		return LeagueParticipantResponse.entityToLeagueParticipantResponse(leagueParticipation);
	}

	private void checkSilverMemberTIer(MemberTier clubMemberTier, Long leagueId, Long clubMemberId) {
		if (clubMemberTier == MemberTier.BRONZE) {
			throw new InsufficientTierException(MemberTier.SILVER, leagueId, clubMemberId);
		}
	}

	private void checkGoldMemberTier(MemberTier clubMemberTier, Long leagueId, Long clubMemberId) {
		if (clubMemberTier != MemberTier.GOLD) {
			throw new InsufficientTierException(MemberTier.GOLD, leagueId, clubMemberId);
		}
	}
	private void checkLeagueRecruitingStatus (LeagueEntity league){
		if (league.getLeagueStatus() == LeagueStatus.COMPLETED)
			throw new LeagueRecruitingCompletedException(league.getLeagueId(), league.getLeagueStatus(),
				league.getPlayerLimitCount());
	}

		public LeagueParticipationCancelResponse cancelLeagueParticipation (Long clubId, Long leagueId, Long memberId){

			Long clubMemberId = provideClubMemberIfClubMemberInClub(clubId, memberId).getClubMemberId();
			LeagueParticipantEntity leagueParticipant = provideLeagueParticipantIfClubMemberInLeague(leagueId,
				clubMemberId);

			if (leagueParticipant.isCanceled())
				throw new LeagueParticipationAlreadyCanceledException(leagueId, memberId);

			leagueParticipant.cancelLeagueParticipation();
			leagueParticipantRepository.save(leagueParticipant);

			return LeagueParticipationCancelResponse.entityToLeagueParticipationCancelResponse(leagueParticipant);
		}

		private void checkIfClubMemberInLeague (Long leagueId, Long clubMemberId){
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

		private LeagueEntity provideLeagueIfClubMemberInLeague (Long clubId, Long leagueId){
			return leagueRepository.findByClubClubIdAndLeagueId(clubId, leagueId).orElseThrow(
				() -> new LeagueNotExistException(clubId, leagueId));
		}

		private LeagueParticipantEntity provideLeagueParticipantIfClubMemberInLeague (Long leagueId, Long clubMemberId){
			return leagueParticipantRepository.findByLeague_LeagueIdAndClubMember_ClubMemberId(leagueId, clubMemberId)
				.orElseThrow(
					() -> new LeagueParticipationNotExistException(leagueId, clubMemberId));
		}

		// TODO: ban 당하거나 탈퇴한 회원의 경우 조회에서 제외
		private ClubMemberEntity provideClubMemberIfClubMemberInClub (Long clubId, Long memberId){
			return clubMemberRepository.findByClub_ClubIdAndMember_MemberId(clubId, memberId).orElseThrow(
				() -> new ClubMemberNotExistException(clubId, memberId));
		}

		private void checkPlayerCount (LeagueEntity league){
			// TODO: MatchCreateService와 중복 코드 발생
			List<LeagueParticipantEntity> leagueParticipantList =
				leagueParticipantRepository.findAllByLeague_LeagueId(league.getLeagueId());
			if (league.getPlayerLimitCount() == leagueParticipantList.size()) {
				league.completeLeagueRecruiting();
			}
		}

	}

