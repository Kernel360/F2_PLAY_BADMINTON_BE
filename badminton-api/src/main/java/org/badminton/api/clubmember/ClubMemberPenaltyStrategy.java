package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.ClubMemberBanRecordResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public interface ClubMemberPenaltyStrategy {
	ClubMemberBanRecordResponse execute(ClubMemberEntity clubMember, Object request);

}
