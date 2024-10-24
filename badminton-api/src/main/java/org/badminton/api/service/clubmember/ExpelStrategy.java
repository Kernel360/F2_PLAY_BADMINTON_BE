// package org.badminton.api.service.clubmember;
//
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberExpelRequest;
// import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecord;
// import org.badminton.domain.domain.clubmember.entity.ClubMember;
// import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
//
// public class ExpelStrategy extends AbstractClubMemberBanStrategy {
//
//     public ExpelStrategy(ClubMemberRepository clubMemberRepository) {
//         super(clubMemberRepository);
//     }
//
//     @Override
//     protected ClubMember.BannedType getBannedType(Object request) {
//         return ClubMember.BannedType.PERMANENT;
//     }
//
//     @Override
//     protected String getReason(Object request) {
//         return ((ClubMemberExpelRequest) request).expelReason();
//
//     }
//
//     @Override
//     protected ClubMemberBanRecord createBanRecord(ClubMember clubMember, ClubMember.BannedType bannedType,
//                                                         String reason) {
//         return new ClubMemberBanRecord(clubMember, ClubMember.BannedType.PERMANENT, reason);
//     }
//
//     @Override
//     protected void applyExpel(ClubMember clubMember, ClubMember.BannedType bannedType) {
//         clubMember.expel();
//     }
// }
