package org.badminton.api.match.validator.policy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomMatchingPolicy implements MatchingPolicy {
	@Override
	public HashMap<Long, Long> match(List<Long> memberIds) {

		Collections.shuffle(memberIds, new Random());

		HashMap<Long, Long> result = new HashMap<>();

		for (int i = 0; i < memberIds.size(); i = i + 2) {
			if (memberIds.size() % 2 != 0 && i == memberIds.size() - 1) {
				result.put(memberIds.get(i), memberIds.get(i));
				break;
			}
			Long first = memberIds.get(i);
			Long second = memberIds.get(i + 1);
			result.put(first, second);
		}
		return result;
	}
}
