package org.badminton.api.league.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.badminton.api.common.exception.club.ClubNotExistException;
import org.badminton.api.common.exception.league.InvalidDateTimeException;
import org.badminton.api.common.exception.league.LeagueNotExistException;
import org.badminton.api.league.model.dto.LeagueByDateResponse;
import org.badminton.api.league.model.dto.LeagueCreateRequest;
import org.badminton.api.league.model.dto.LeagueCreateResponse;
import org.badminton.api.league.model.dto.LeagueDetailsResponse;
import org.badminton.api.league.model.dto.LeagueReadResponse;
import org.badminton.api.league.model.dto.LeagueUpdateRequest;
import org.badminton.api.league.model.dto.LeagueUpdateResponse;
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
			leagueCreateRequest.closedAt(), leagueCreateRequest.playerCount(),
			leagueCreateRequest.matchType(), leagueCreateRequest.matchGenerationType(), club);

		return LeagueCreateResponse.leagueCreateEntityToResponse(leagueRepository.save(league));
	}

	public LeagueDetailsResponse getLeague(Long clubId, Long leagueId, Long memberId) {
		boolean isParticipatedInLeague = false;
		if (memberId != null) {
			isParticipatedInLeague = leagueParticipantRepository
				.findByMemberMemberIdAndCanceledFalse(memberId)
				.isPresent();
		}
		ClubEntity club = provideClub(clubId);
		LeagueEntity league = provideLeagueIfLeagueInClub(club.getClubId(), leagueId);
		int recruitedMemberCount = leagueParticipantRepository.countByLeagueLeagueId(leagueId);
		return LeagueDetailsResponse.fromLeagueEntityAndRecruitedMemberCountAndIsParticipated(league,
			recruitedMemberCount, isParticipatedInLeague);
	}

	public List<LeagueReadResponse> getLeaguesByMonth(Long clubId, String date) {
		if (!validateDate(date)) {
			throw new InvalidDateTimeException(date);
		}

		LocalDate parsedDate = parseDateByMonth(date);
		LocalDateTime startOfMonth = getStartOfMonth(parsedDate);
		LocalDateTime endOfMonth = getEndOfMonth(parsedDate);

		List<LeagueEntity> result = leagueRepository.findAllByClubClubIdAndLeagueAtBetween(clubId, startOfMonth,
			endOfMonth);

		return result.stream()
			.map(LeagueReadResponse::leagueReadEntityToResponse)
			.collect(
				Collectors.toList());
	}

	public List<LeagueByDateResponse> getLeaguesByDate(Long clubId, String date) {
		if (!validateDate(date)) {
			throw new InvalidDateTimeException(date);
		}

		LocalDate parsedDate = parseDateByDate(date);
		LocalDateTime startOfDay = getStartOfDay(parsedDate);
		LocalDateTime endOfDay = getEndOfDay(parsedDate);

		List<LeagueEntity> leaguesByDate = leagueRepository.findAllByClubClubIdAndLeagueAtBetween(clubId, startOfDay,
			endOfDay);

		return leaguesByDate.stream()
			.map(league -> LeagueByDateResponse.fromLeagueEntity(league,
				leagueParticipantRepository.findAllByLeagueLeagueIdAndCanceledFalse(league.getLeagueId()).size()))
			.collect(Collectors.toList());
	}

	public LeagueUpdateResponse updateLeague(Long clubId, Long leagueId, LeagueUpdateRequest leagueUpdateRequest
	) {
		LeagueEntity league = provideLeagueIfLeagueInClub(clubId, leagueId);
		// TODO: 개선 방법, 변경할 수 있는 필드에 제한
		league.updateLeague(leagueUpdateRequest.leagueName(),
			leagueUpdateRequest.description(), leagueUpdateRequest.leagueLocation(), leagueUpdateRequest.tierLimit(),
			leagueUpdateRequest.leagueAt(), leagueUpdateRequest.closedAt(),
			leagueUpdateRequest.playerCount(),
			leagueUpdateRequest.matchType(), leagueUpdateRequest.matchGenerationType());

		int recruitedMemberCount = leagueParticipantRepository.countByLeagueLeagueId(leagueId);
		leagueRepository.save(league);
		return LeagueUpdateResponse.fromLeagueEntityAndRecruitedMemberCountAndIsParticipated(league,
			recruitedMemberCount);
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

	private LocalDate parseDateByMonth(String date) {
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

	private LocalDate parseDateByDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date, formatter);
	}

	private LocalDateTime getStartOfDay(LocalDate date) {
		return date.atStartOfDay();
	}

	private LocalDateTime getEndOfDay(LocalDate date) {
		return date.atTime(23, 59, 59);
	}

}
