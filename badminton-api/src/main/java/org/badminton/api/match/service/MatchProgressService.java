package org.badminton.api.match.service;

import org.badminton.domain.league.entity.LeagueEntity;
import org.badminton.domain.match.repository.DoublesMatchResultRepository;
import org.badminton.domain.match.repository.DoublesSetRepository;
import org.badminton.domain.match.repository.SinglesMatchResultRepository;
import org.badminton.domain.match.repository.SinglesSetRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchProgressService {
	private final SinglesSetRepository singlesSetRepository;
	private final DoublesSetRepository doublesSetRepository;
	private final SinglesMatchResultRepository singlesMatchResultRepository;
	private final DoublesMatchResultRepository doublesMatchResultRepository;


	public void startMatch(Long matchId, ) {

	}
}
