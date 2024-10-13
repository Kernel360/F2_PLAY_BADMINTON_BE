package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.clubMemberBanRecordResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public interface ClubMemberPenaltyStrategy {
	clubMemberBanRecordResponse execute(ClubMemberEntity clubMember, Object request);

}
