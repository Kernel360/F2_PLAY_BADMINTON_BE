package org.badminton.api.league.service;

import org.badminton.domain.club.repository.ClubRepository;
import org.badminton.domain.league.repository.LeagueRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

	@Mock
	private LeagueRepository leagueRepository;

	@Mock
	private ClubRepository clubRepository;

	@InjectMocks
	private LeagueService leagueService;

	@Test
	@DisplayName("경기 생성을 테스트합니다.")
	void createLeague() {
		//given
		//when
		//then
	}

	@Test
	void getLeague() {
	}

	@Test
	void updateLeague() {
	}

	@Test
	void cancelLeague() {
	}
}
