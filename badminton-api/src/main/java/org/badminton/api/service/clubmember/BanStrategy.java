// package org.badminton.api.service.clubmember;
//
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRequest;
// import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecord;
// import org.badminton.domain.domain.clubmember.entity.ClubMember;
// import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
//
// public class BanStrategy extends AbstractClubMemberBanStrategy {
//
//     public BanStrategy(ClubMemberRepository clubMemberRepository) {
//         super(clubMemberRepository);
//     }
//
//     @Override
//     protected ClubMember.BannedType getBannedType(Object request) {
//         return ((ClubMemberBanRequest) request).type();
//     }
//
//     @Override
//     protected String getReason(Object request) {
//         return ((ClubMemberBanRequest) request).bannedReason();
//     }
//
//     @Override
//     protected ClubMemberBanRecord createBanRecord(ClubMember clubMember, ClubMember.BannedType bannedType,
//                                                         String reason) {
//         return new ClubMemberBanRecord(clubMember, bannedType, reason);
//     }
//
//     @Override
//     protected void applyExpel(ClubMember clubMember, ClubMember.BannedType bannedType) {
//     }
// }