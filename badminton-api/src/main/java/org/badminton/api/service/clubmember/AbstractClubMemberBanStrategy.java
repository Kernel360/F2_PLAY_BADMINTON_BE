// package org.badminton.api.service.clubmember;
//
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRecordResponse;
// import org.badminton.domain.common.exception.clubmember.ClubMemberAlreadyBannedException;
// import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecord;
// import org.badminton.domain.domain.clubmember.entity.ClubMember;
// import org.badminton.domain.infrastructures.clubmember.ClubMemberRepository;
//
// public abstract class AbstractClubMemberBanStrategy implements ClubMemberPenaltyStrategy {
//     protected final ClubMemberRepository clubMemberRepository;
//
//     protected AbstractClubMemberBanStrategy(ClubMemberRepository clubMemberRepository) {
//         this.clubMemberRepository = clubMemberRepository;
//     }
//
//     @Override
//     public ClubMemberBanRecordResponse execute(ClubMember clubMember, Object request) {
//         validateMemberNotBanned(clubMember);
//         ClubMember.BannedType bannedType = getBannedType(request);
//         String reason = getReason(request);
//         ClubMemberBanRecord newBanRecord = createBanRecord(clubMember, bannedType, reason);
//         clubMember.addBanRecord(newBanRecord);
//         applyExpel(clubMember, bannedType);
//         clubMemberRepository.save(clubMember);
//         return ClubMemberBanRecordResponse.entityToClubMemberBanRecordResponse(newBanRecord);
//     }
//
//     protected abstract ClubMember.BannedType getBannedType(Object request);
//
//     protected abstract String getReason(Object request);
//
//     protected abstract ClubMemberBanRecord createBanRecord(ClubMember clubMember, ClubMember.BannedType bannedType,
//                                                                  String reason);
//
//     protected abstract void applyExpel(ClubMember clubMember, ClubMember.BannedType bannedType);
//
//     private void validateMemberNotBanned(ClubMember clubMember) {
//         clubMember.getBanHistory()
//                 .stream()
//                 .filter(ClubMemberBanRecord::isActive)
//                 .findFirst()
//                 .ifPresent(ban -> {
//                     throw new ClubMemberAlreadyBannedException(clubMember.getClubMemberId());
//                 });
//     }
//
// }
