package org.badminton.api.league.validator;

import static org.badminton.api.common.error.LeagueErrorCode.*;

import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LeagueValidator {
	private final LeagueRepository leagueRepository;

	public void checkIfLeagueExists(String leagueName) {
		leagueRepository.findByLeagueName(leagueName).ifPresent(league -> {
			throw new BadmintonException(LEAGUE_ALREADY_EXISTS);
		});
	}
}
