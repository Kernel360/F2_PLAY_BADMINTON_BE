package org.badminton.domain.clubmember.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long> {

	List<ClubMemberEntity> findAllByClub_ClubId(Long clubId);

	Optional<ClubMemberEntity> findByMember_MemberId(Long memberId);

	Optional<ClubMemberEntity> findByClub_ClubIdAndMember_MemberId(Long clubId, Long memberId);

	boolean existsByMember_MemberId(Long memberId);

	List<ClubMemberEntity> findAllByMember_MemberId(Long memberId);

	boolean existsByMember_MemberIdAndClub_ClubId(Long memberId, Long clubId);
}

