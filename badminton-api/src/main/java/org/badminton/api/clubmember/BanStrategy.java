package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.ClubMemberBanRequest;
import org.badminton.domain.clubmember.entity.BannedClubMemberEntity;
import org.badminton.domain.clubmember.entity.BannedType;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;

public class BanStrategy extends AbstractClubMemberBanStrategy {

	public BanStrategy(ClubMemberRepository clubMemberRepository) {
		super(clubMemberRepository);
	}

	@Override
	protected BannedType getBannedType(Object request) {
		return ((ClubMemberBanRequest)request).type();
	}

	@Override
	protected String getReason(Object request) {
		return ((ClubMemberBanRequest)request).bannedReason();
	}

	@Override
	protected BannedClubMemberEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType,
		String reason) {
		return new BannedClubMemberEntity(clubMember, bannedType, reason);
	}

	@Override
	protected void applyExpel(ClubMemberEntity clubMember, BannedType bannedType) {
	}
}