package org.badminton.domain.member.repository;

import org.badminton.domain.member.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	MemberEntity findByProviderId(String providerId);
}
