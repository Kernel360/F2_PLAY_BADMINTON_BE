package org.badminton.api.service.clubmember;

import org.badminton.api.interfaces.clubmember.dto.ClubMemberExpelRequest;
import org.badminton.domain.domain.clubmember.entity.BannedType;
import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.infra.clubmember.ClubMemberRepository;

public class ExpelStrategy extends AbstractClubMemberBanStrategy {

    public ExpelStrategy(ClubMemberRepository clubMemberRepository) {
        super(clubMemberRepository);
    }

    @Override
    protected BannedType getBannedType(Object request) {
        return BannedType.PERMANENT;
    }

    @Override
    protected String getReason(Object request) {
        return ((ClubMemberExpelRequest) request).expelReason();

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
