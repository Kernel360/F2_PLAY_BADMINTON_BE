package org.badminton.api.match.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.badminton.api.match.validator.SinglesMatchValidator;
import org.badminton.api.match.validator.policy.MatchingPolicy;
import org.badminton.api.match.validator.policy.MatchingPolicyType;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SinglesMatchService {
	private final SinglesMatchRepository singlesMatchRepository;
	private final SinglesMatchValidator singlesMatchValidator;

	public void singlesMatchCreate(Long leagueId) {
		// league 정책을 불러옴
		String matchingRequirement = "RANDOM";

		//leagueId로 경기를 하는 사람 리스트를 불러옴
		List<Long> memberIds = new ArrayList<>();
		memberIds.add(1L);
		memberIds.add(2L);
		memberIds.add(3L);
		memberIds.add(4L);
		memberIds.add(5L);

		MatchingPolicy matchingPolicy = singlesMatchValidator.checkMatchPolicy(
			MatchingPolicyType.valueOf(matchingRequirement));

		HashMap<Long, Long> matchResult = matchingPolicy.match(memberIds);

		for (Map.Entry<Long, Long> id : matchResult.entrySet()) {
			Long firstId = id.getKey();
			Long secondId = id.getValue();
			if (Objects.equals(firstId, secondId)) {
				singlesMatchValidator.checkValidationAndSave(leagueId, firstId, secondId);
				continue;
			}
			singlesMatchValidator.checkValidationAndSave(leagueId, firstId, secondId);
			singlesMatchValidator.checkValidationAndSave(leagueId, secondId, firstId);

		}
	}
}
