package org.badminton.api.member.service;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;

	public MemberResponse updateMember(HttpServletRequest request, MemberUpdateRequest memberUpdateRequest) {

		String providerId = jwtUtil.extractProviderIdFromRequest(request);
		MemberEntity memberEntity = memberRepository.findByProviderId(providerId);

		memberEntity.updateMember(memberUpdateRequest.getProfileImage());
		memberRepository.save(memberEntity);
		return MemberResponse.memberEntityToResponse(memberEntity);
	}

}
