package org.badminton.domain.common.exception.clubmember;

import org.badminton.domain.common.error.ErrorCode;
import org.badminton.domain.common.exception.BadmintonException;

public class ClubMemberDuplicateException extends BadmintonException {

    public ClubMemberDuplicateException(Long clubId, Long memberId) {
        super(ErrorCode.CLUB_MEMBER_ALREADY_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberId + "]");
    }

    public ClubMemberDuplicateException(Long clubId, Long memberId, Exception e) {
        super(ErrorCode.CLUB_MEMBER_ALREADY_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberId + "]", e);
    }
}
