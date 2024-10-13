package org.badminton.api.clubmember;

import org.badminton.api.clubmember.model.dto.clubMemberBanRecordResponse;
import org.badminton.api.common.exception.clubmember.ClubMemberAlreadyBannedException;
import org.badminton.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.clubmember.entity.BannedType;
import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.clubmember.repository.ClubMemberRepository;

public abstract class AbstractClubMemberBanStrategy implements ClubMemberPenaltyStrategy{
	protected final ClubMemberRepository clubMemberRepository;

	protected AbstractClubMemberBanStrategy(ClubMemberRepository clubMemberRepository) {
		this.clubMemberRepository = clubMemberRepository;
	}


	@Override
	public clubMemberBanRecordResponse execute(ClubMemberEntity clubMember, Object request) {
		validateMemberNotBanned(clubMember);
		BannedType bannedType = getBannedType(request);
		String reason = getReason(request);
		ClubMemberBanRecordEntity newBanRecord = createBanRecord(clubMember, bannedType, reason);
		clubMember.addBanRecord(newBanRecord);
		applyExpel(clubMember, bannedType);
		clubMemberRepository.save(clubMember);
		return clubMemberBanRecordResponse.entityToClubMemberBanRecordResponse(newBanRecord);
	}

	protected abstract BannedType getBannedType(Object request);
	protected abstract String getReason(Object request);
	protected abstract ClubMemberBanRecordEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType, String reason);
	protected abstract void applyExpel(ClubMemberEntity clubMember, BannedType bannedType);

	private void validateMemberNotBanned(ClubMemberEntity clubMember) {
		clubMember.getBanHistory()
			.stream()
			.filter(ClubMemberBanRecordEntity::isActive)
			.findFirst()
			.ifPresent(ban -> {
				throw new ClubMemberAlreadyBannedException(clubMember.getClubMemberId());
			});
	}



}
