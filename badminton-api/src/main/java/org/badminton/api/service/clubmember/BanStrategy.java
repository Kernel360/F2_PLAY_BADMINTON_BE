package org.badminton.api.service.clubmember;

import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRequest;
import org.badminton.domain.domain.clubmember.entity.BannedType;
import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.infra.clubmember.ClubMemberRepository;

public class BanStrategy extends AbstractClubMemberBanStrategy {

    public BanStrategy(ClubMemberRepository clubMemberRepository) {
        super(clubMemberRepository);
    }

    @Override
    protected BannedType getBannedType(Object request) {
        return ((ClubMemberBanRequest) request).type();
    }

    @Override
    protected String getReason(Object request) {
        return ((ClubMemberBanRequest) request).bannedReason();
    }

    @Override
    protected ClubMemberBanRecordEntity createBanRecord(ClubMemberEntity clubMember, BannedType bannedType,
                                                        String reason) {
        return new ClubMemberBanRecordEntity(clubMember, bannedType, reason);
    }

    @Override
    protected void applyExpel(ClubMemberEntity clubMember, BannedType bannedType) {
    }
}