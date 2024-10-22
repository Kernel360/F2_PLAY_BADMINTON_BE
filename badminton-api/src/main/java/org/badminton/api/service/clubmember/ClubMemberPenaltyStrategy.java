package org.badminton.api.service.clubmember;

import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRecordResponse;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

public interface ClubMemberPenaltyStrategy {
    ClubMemberBanRecordResponse execute(ClubMemberEntity clubMember, Object request);

}
