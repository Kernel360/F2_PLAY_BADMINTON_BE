package org.badminton.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findByProviderId(String providerId);

	Optional<MemberEntity> findByMemberId(Long memberId);

	List<MemberEntity> findAllByIsDeletedTrue();
}
