package org.badminton.api.common.exception.member;

import org.badminton.domain.common.error.ErrorCode;
import org.badminton.domain.common.exception.BadmintonException;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;

public class MemberAlreadyExistInClubException extends BadmintonException {

    public MemberAlreadyExistInClubException(Long memberId, Long clubId, ClubMemberRole clubMemberRole) {
        super(ErrorCode.MEMBER_ALREADY_JOINED_CLUB,
                "[회원 아이디 : " + memberId + " 동호회 아이디 : " + clubId + " 동호회 역할 : "
                        + clubMemberRole.getDescription() + "]");
    }

    public MemberAlreadyExistInClubException(Long memberId, Long clubId, ClubMemberRole clubMemberRole, Exception e) {
        super(ErrorCode.MEMBER_ALREADY_JOINED_CLUB,
                "[회원 아이디 : " + memberId + " 동호회 아이디 : " + clubId + " 동호회 역할 : "
                        + clubMemberRole.getDescription() + "]", e);
    }

    public MemberAlreadyExistInClubException(Long memberId, String memberName, String clubName,
                                             ClubMemberRole clubMemberRole) {
        super(ErrorCode.MEMBER_ALREADY_JOINED_CLUB,
                "[회원 아이디 : " + memberId + " 닉네임 : " + memberName + " 동호회 이름 : " + clubName + " 동호회 역할 : "
                        + clubMemberRole.getDescription() + "]");
    }

    public MemberAlreadyExistInClubException(Long memberId, String memberName, String clubName,
                                             ClubMemberRole clubMemberRole, Exception e) {
        super(ErrorCode.MEMBER_ALREADY_JOINED_CLUB,
                "[회원 아이디 : " + memberId + " 닉네임 : " + memberName + " 동호회 이름 : " + clubName + " 동호회 역할 : "
                        + clubMemberRole.getDescription() + "]", e);
    }

}
