package org.badminton.domain.domain.member;

import org.badminton.domain.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberReader memberReader;

	@Override
	public MemberEntity getMemberByMemberId(Long memberId) {
		return memberReader.getMemberByMemberId(memberId);
	}
}
