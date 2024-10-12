package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.BannedClubMemberResponse;
import org.badminton.api.common.exception.clubmember.ClubMemberAlreadyBannedException;
import org.badminton.domain.clubmember.entity.BannedClubMemberEntity;
import org.badminton.domain.clubmember.entity.BannedType;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;

public abstract class AbstractClubMemberBanStrategy implements ClubMemberPenaltyStrategy{
	protected final ClubMemberRepository clubMemberRepository;

	protected AbstractClubMemberBanStrategy(ClubMemberRepository clubMemberRepository) {
		this.clubMemberRepository = clubMemberRepository;
	}


	@Override
	public BannedClubMemberResponse execute(ClubMemberEntity clubMember, Object request) {
		validateMemberNotBanned(clubMember);
		BannedType bannedType = getBannedType(request);
		String reason = getReason(request);
		BannedClubMemberEntity newBanRecord = createBanRecord(clubMember, bannedType, reason);
		clubMember.addBanRecord(newBanRecord);
		applyExpel(clubMember, bannedType);
		clubMemberRepository.save(clubMember);
		return BannedClubMemberResponse.entityToBannedClubMemberResponse(newBanRecord);
	}

	protected abstract BannedType getBannedType(Object request);
	protected abstract String getReason(Object request);
	protected abstract BannedClubMemberEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType, String reason);
	protected abstract void applyExpel(ClubMemberEntity clubMember, BannedType bannedType);

	private void validateMemberNotBanned(ClubMemberEntity clubMember) {
		clubMember.getBanHistory()
			.stream()
			.filter(BannedClubMemberEntity::isActive)
			.findFirst()
			.ifPresent(ban -> {
				throw new ClubMemberAlreadyBannedException(clubMember.getClubMemberId());
			});
	}



}
