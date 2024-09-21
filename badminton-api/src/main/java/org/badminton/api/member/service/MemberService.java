package org.badminton.api.member.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.model.dto.MemberResponse;
import org.badminton.api.member.model.dto.MemberUpdateRequest;
import org.badminton.domain.member.entity.MemberAuthorization;
import org.badminton.domain.member.entity.MemberEntity;
import org.badminton.domain.member.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
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

	public void markAsDeleted(String providerId) {
		MemberEntity memberEntity = memberRepository.findByProviderId(providerId);

		memberEntity.deleteMember();
		memberRepository.save(memberEntity);
		log.info("Member marked as deleted: {}", providerId);
	}

	public MemberEntity handleLogin(String providerId, String email, String name, String profileImage,
		String authorization) {
		MemberEntity memberEntity = memberRepository.findByProviderId(providerId);

		// 기존 회원이 존재하고 isDeleted가 true인 경우, 계정을 다시 활성화
		if (memberEntity != null) {
			if (memberEntity.isDeleted()) {
				memberEntity.reactivateMember();
				log.info("Reactivating member: {}", providerId);
			}
			memberEntity.updateMember(profileImage);
		} else {
			// 신규 회원 생성
			memberEntity = new MemberEntity(email, name, providerId, profileImage,
				MemberAuthorization.valueOf(authorization));
		}

		memberRepository.save(memberEntity);
		return memberEntity;
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void DeleteMembers() {
		log.info("Deleting members");

		List<MemberEntity> deleteMembers = memberRepository.findAllByIsAndMemberDeletedTrue();

		for (MemberEntity memberEntity : deleteMembers) {
			LocalDateTime lastConnectionAt = memberEntity.getLastConnectionAt();
			if (lastConnectionAt != null) {
				if (ChronoUnit.DAYS.between(lastConnectionAt, LocalDateTime.now()) > 7) {
					memberRepository.delete(memberEntity);
					log.info("Delete member: {}", memberEntity.getProviderId());
				}
			}
		}
		log.info("Deleting members");
	}

}
