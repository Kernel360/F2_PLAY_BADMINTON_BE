package org.badminton.domain.common.exception.clubmember;

import org.badminton.domain.common.error.ErrorCode;
import org.badminton.domain.common.exception.BadmintonException;

public class ClubMemberDuplicateException extends BadmintonException {

    public ClubMemberDuplicateException(Long clubId, String memberToken) {
        super(ErrorCode.CLUB_MEMBER_ALREADY_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberToken + "]");
    }

    public ClubMemberDuplicateException(Long clubId, String memberToken, Exception e) {
        super(ErrorCode.CLUB_MEMBER_ALREADY_EXIST, "[동호회 아이디 : " + clubId + " 회원 아이디 : " + memberToken + "]", e);
    }
}
