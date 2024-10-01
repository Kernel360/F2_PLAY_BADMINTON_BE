package org.badminton.api.member.validator;

import org.badminton.api.common.exception.member.MemberNotExistException;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberValidator {

	private final MemberRepository memberRepository;

	public MemberEntity findMemberByMemberId(Long memberId) {
		return memberRepository.findByMemberId(memberId).orElseThrow(() ->
			new MemberNotExistException(memberId));
	}

}

