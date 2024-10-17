package org.badminton.api.match.service;

import java.util.ArrayList;
import java.util.List;

import org.badminton.api.match.model.dto.MatchResultResponse;
import org.badminton.domain.match.model.entity.DoublesMatchEntity;
import org.badminton.domain.match.model.entity.SinglesMatchEntity;
import org.badminton.domain.match.repository.DoublesMatchRepository;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchResultService {

	private final SinglesMatchRepository singlesMatchRepository;
	private final DoublesMatchRepository doublesMatchRepository;

	public List<MatchResultResponse> getAllMatchResultsByClubMember(Long clubMemberId) {
		List<MatchResultResponse> allResults = new ArrayList<>();

		// 단식 경기 결과 가져오기
		List<SinglesMatchEntity> singlesMatches = singlesMatchRepository.findAllCompletedByClubMemberId(clubMemberId);
		for (SinglesMatchEntity match : singlesMatches) {
			allResults.add(MatchResultResponse.fromSinglesMatchEntity(match, clubMemberId));
		}

		// 복식 경기 결과 가져오기
		List<DoublesMatchEntity> doublesMatches = doublesMatchRepository.findAllCompletedByClubMemberId(clubMemberId);
		for (DoublesMatchEntity match : doublesMatches) {
			allResults.add(MatchResultResponse.fromDoublesMatchEntity(match, clubMemberId));
		}

		allResults.sort((r1, r2) -> r2.leagueAt().compareTo(r1.leagueAt()));
		return allResults;
	}
}
