package org.badminton.api.match.validator.policy;

import java.util.HashMap;
import java.util.List;

public interface MatchingPolicy {
	HashMap<Long, Long> match(List<Long> memberIds);
}
