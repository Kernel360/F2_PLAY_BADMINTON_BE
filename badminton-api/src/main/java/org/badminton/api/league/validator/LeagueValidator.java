package org.badminton.api.league.validator;

import org.badminton.domain.league.repository.LeagueRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LeagueValidator {
	private final LeagueRepository leagueRepository;

}
