package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.BannedClubMemberResponse;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;

public interface ClubMemberPenaltyStrategy {
	BannedClubMemberResponse execute(ClubMemberEntity clubMember, Object request);

}
