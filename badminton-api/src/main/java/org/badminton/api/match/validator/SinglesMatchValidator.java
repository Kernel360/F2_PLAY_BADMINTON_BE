package org.badminton.api.match.validator;

import java.util.HashMap;
import java.util.Map;

import org.badminton.api.match.model.dto.SinglesMatchCreateRequest;
import org.badminton.api.match.validator.policy.MatchingPolicy;
import org.badminton.api.match.validator.policy.MatchingPolicyType;
import org.badminton.api.match.validator.policy.RandomMatchingPolicy;
import org.badminton.domain.match.entity.SinglesMatchEntity;
import org.badminton.domain.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SinglesMatchValidator {
	//TODO : 검증 코드 추가

	private final SinglesMatchRepository singlesMatchRepository;

	public MatchingPolicy checkMatchPolicy(MatchingPolicyType policyType) {
		Map<MatchingPolicyType, MatchingPolicy> policy = new HashMap<>();
		policy.put(MatchingPolicyType.RANDOM, new RandomMatchingPolicy());

		return policy.get(policyType);
	}

	public void checkValidationAndSave(Long leagueId, Long memberId, Long oppositeId) {
		SinglesMatchCreateRequest request = new SinglesMatchCreateRequest(
			leagueId,
			memberId,
			oppositeId,
			"BEFORE",
			0L,
			0L,
			0L);

		SinglesMatchEntity entity = SinglesMatchCreateRequest.singlesMatchCreateRequestToEntity(request);

		singlesMatchRepository.save(entity);
	}
}
