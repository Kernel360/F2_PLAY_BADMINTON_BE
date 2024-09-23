package org.badminton.domain.member.repository;

import java.util.List;

import org.badminton.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	MemberEntity findByProviderId(String providerId);

	List<MemberEntity> findAllByIsDeletedTrue();
}
