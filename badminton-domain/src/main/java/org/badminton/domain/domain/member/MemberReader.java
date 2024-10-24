package org.badminton.domain.domain.member;

import org.badminton.domain.domain.member.entity.MemberEntity;

public interface MemberReader {
	MemberEntity getMemberByMemberId(Long memberId);
}
