package org.badminton.api.league.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.league.InvalidDateTimeException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.league.model.dto.LeagueAndParticipantResponse;
import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueStatusUpdateResponse;
import org.badminton.api.league.model.dto.LeagueUpdateRequest;
import org.badminton.api.league.model.enums.EndDateType;
import org.badminton.api.league.model.enums.StartDateType;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.league.repository.LeagueParticipantRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class LeagueService {
	private static final Integer YEAR_TO_FROM = 20;

	private final LeagueRepository leagueRepository;
	private final ClubRepository clubRepository;
	private final LeagueParticipantRepository leagueParticipantRepository;

	public LeagueCreateResponse createLeague(Long clubId, LeagueCreateRequest leagueCreateRequest) {

		ClubEntity club = provideClub(clubId);

		// TODO: 개선 방법
		LeagueEntity league = new LeagueEntity(leagueCreateRequest.leagueName(),
			leagueCreateRequest.description(), leagueCreateRequest.leagueLocation(), leagueCreateRequest.leagueAt(),
			leagueCreateRequest.tierLimit(),
			leagueCreateRequest.closedAt(), leagueCreateRequest.leagueStatus(), leagueCreateRequest.playerCount(),
			leagueCreateRequest.matchType(), leagueCreateRequest.matchGenerationType(), club);

		return LeagueCreateResponse.leagueCreateEntityToResponse(leagueRepository.save(league));
	}

	public LeagueAndParticipantResponse getLeague(Long clubId, Long leagueId) {
		ClubEntity club = provideClub(clubId);
		LeagueEntity league = provideLeagueIfLeagueInClub(club.getClubId(), leagueId);
		int participateCount = leagueParticipantRepository.countByLeagueLeagueId(leagueId);

		return LeagueAndParticipantResponse.leagueAndParticipantEntityToResponse(league, participateCount);
	}

	public List<LeagueReadResponse> getLeagues(Long clubId, String date) {
		if (!validateDate(date)) {
			throw new InvalidDateTimeException(date);
		}

		LocalDate parsedDate = parseDate(date);
		LocalDateTime startOfMonth = getStartOfMonth(parsedDate);
		LocalDateTime endOfMonth = getEndOfMonth(parsedDate);

		List<LeagueEntity> result = leagueRepository.findAllByClubClubIdAndLeagueAtBetween(clubId, startOfMonth,
			endOfMonth);

		return result.stream()
			.map(LeagueReadResponse::leagueReadEntityToResponse)
			.collect(
				Collectors.toList());
	}

	public LeagueStatusUpdateResponse updateLeague(Long clubId, Long leagueId, LeagueUpdateRequest leagueUpdateRequest
	) {
		LeagueEntity league = provideLeagueIfLeagueInClub(clubId, leagueId);

		// TODO: 개선 방법
		league.updateLeague(leagueUpdateRequest.leagueName(),
			leagueUpdateRequest.description(), leagueUpdateRequest.leagueLocation(), leagueUpdateRequest.tierLimit(),
			leagueUpdateRequest.leagueAt(), leagueUpdateRequest.closedAt(), leagueUpdateRequest.leagueStatus(),
			leagueUpdateRequest.playerCount(),
			leagueUpdateRequest.matchType(), leagueUpdateRequest.matchGenerationType());

		leagueRepository.save(league);
		return new LeagueStatusUpdateResponse(league);
	}

	@Transactional
	public void deleteLeague(Long clubId, Long leagueId) {
		LeagueEntity league = provideLeagueIfLeagueInClub(clubId, leagueId);
		leagueRepository.deleteByLeagueId(league.getLeagueId());
	}

	private LeagueEntity provideLeagueIfLeagueInClub(Long clubId, Long leagueId) {
		return leagueRepository.findByClubClubIdAndLeagueId(clubId, leagueId).orElseThrow(
			() -> new LeagueNotExistException(clubId, leagueId));
	}

	private ClubEntity provideClub(Long clubId) {
		return clubRepository.findByClubIdAndIsClubDeletedFalse(clubId).orElseThrow(
			() -> new ClubNotExistException(clubId));
	}

	private boolean validateDate(String date) {
		String[] dateForm = date.split("-");
		int year = Integer.parseInt(dateForm[0]);
		int month = Integer.parseInt(dateForm[1]);

		int yearsPrevCompare = LocalDate.now().minusYears(YEAR_TO_FROM).getYear();
		int yearsNextCompare = LocalDate.now().plusYears(YEAR_TO_FROM).getYear();
		if (yearsPrevCompare > year || yearsNextCompare < year) {
			return false;
		}
		return month >= Month.JANUARY.getValue() && month <= Month.DECEMBER.getValue();
	}

	private LocalDate parseDate(String date) {
		String[] parts = date.split("-");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		return LocalDate.of(year, month, StartDateType.START_DAY.getDescription()); // 첫 번째 날로 초기화
	}

	private LocalDateTime getStartOfMonth(LocalDate date) {
		return LocalDateTime.of(date.getYear(), date.getMonthValue(), StartDateType.START_DAY.getDescription(),
			StartDateType.START_HOUR.getDescription(), StartDateType.START_MINUTE.getDescription());
	}

	private LocalDateTime getEndOfMonth(LocalDate date) {
		return LocalDateTime.of(date.getYear(), date.getMonthValue(),
			date.lengthOfMonth(), EndDateType.END_HOUR.getDescription(), EndDateType.END_MINUTE.getDescription());
	}

}
