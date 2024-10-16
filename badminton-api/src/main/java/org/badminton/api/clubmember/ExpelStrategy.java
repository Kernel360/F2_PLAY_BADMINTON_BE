package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.ClubMemberExpelRequest;
import org.badminton.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.clubmember.entity.BannedType;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;

public class ExpelStrategy extends AbstractClubMemberBanStrategy{

	public ExpelStrategy(ClubMemberRepository clubMemberRepository) {
		super(clubMemberRepository);
	}

	@Override
	protected BannedType getBannedType(Object request) {
		return BannedType.PERMANENT;
	}

	@Override
	protected String getReason(Object request) {
		return ((ClubMemberExpelRequest)request).expelReason();

	}

	@Override
	protected ClubMemberBanRecordEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType,
		String reason) {
		return new ClubMemberBanRecordEntity(clubMember, BannedType.PERMANENT, reason);
	}

	@Override
	protected void applyExpel(ClubMemberEntity clubMember, BannedType bannedType) {
		clubMember.expel();
	}
}
