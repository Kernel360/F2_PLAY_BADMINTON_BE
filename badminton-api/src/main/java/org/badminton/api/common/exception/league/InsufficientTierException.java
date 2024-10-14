package org.badminton.api.common.exception.league;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.BadmintonException;
import org.badminton.domain.common.enums.MemberTier;

public class InsufficientTierException extends BadmintonException {
  public InsufficientTierException(MemberTier requiredTier, Long leagueId, Long clubId) {
    super(ErrorCode.INSUFFICIENT_TIER,
        "[기준 티어 : " + requiredTier + ", 리그 아이디 : " + leagueId + ", 회원 아이디 : " + clubId + "]");
  }

  public InsufficientTierException(MemberTier requiredTier, Long leagueId, Long clubId, Exception e) {
    super(ErrorCode.INSUFFICIENT_TIER,
        "[기준 티어 : " + requiredTier + ", 리그 아이디 : " + leagueId + ", 회원 아이디 : " + clubId + "]");
  }
}
