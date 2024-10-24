package org.badminton.domain.infrastructures.member;

import org.badminton.domain.common.exception.member.MemberNotExistException;
import org.badminton.domain.domain.member.MemberReader;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberReadImpl implements MemberReader {
	private final MemberRepository memberRepository;

	@Override
	public MemberEntity getMemberByMemberId(Long memberId) {
		return memberRepository.findByMemberId(memberId)
			.orElseThrow(() -> new MemberNotExistException(memberId));
	}
}
