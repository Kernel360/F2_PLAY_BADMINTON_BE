package org.badminton.api.service.clubmember;

import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRecordResponse;
import org.badminton.domain.common.exception.clubmember.ClubMemberAlreadyBannedException;
import org.badminton.domain.domain.clubmember.entity.BannedType;
import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.infra.clubmember.ClubMemberRepository;

public abstract class AbstractClubMemberBanStrategy implements ClubMemberPenaltyStrategy {
    protected final ClubMemberRepository clubMemberRepository;

    protected AbstractClubMemberBanStrategy(ClubMemberRepository clubMemberRepository) {
        this.clubMemberRepository = clubMemberRepository;
    }

    @Override
    public ClubMemberBanRecordResponse execute(ClubMemberEntity clubMember, Object request) {
        validateMemberNotBanned(clubMember);
        BannedType bannedType = getBannedType(request);
        String reason = getReason(request);
        ClubMemberBanRecordEntity newBanRecord = createBanRecord(clubMember, bannedType, reason);
        clubMember.addBanRecord(newBanRecord);
        applyExpel(clubMember, bannedType);
        clubMemberRepository.save(clubMember);
        return ClubMemberBanRecordResponse.entityToClubMemberBanRecordResponse(newBanRecord);
    }

    protected abstract BannedType getBannedType(Object request);

    protected abstract String getReason(Object request);

    protected abstract ClubMemberBanRecordEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType,
                                                                 String reason);

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
