package org.badminton.domain.clubmember.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long> {

	List<ClubMemberEntity> findAllByClub_ClubId(Long clubId);

	Optional<ClubMemberEntity> findByMember_MemberIdAndDeletedFalse(Long memberId);

	Optional<ClubMemberEntity> findByClub_ClubIdAndMember_MemberId(Long clubId, Long memberId);

	boolean existsByMember_MemberIdAndDeletedFalse(Long memberId);

	List<ClubMemberEntity> findAllByMember_MemberId(Long memberId);

	List<ClubMemberEntity> findAllByClubClubIdAndBannedFalseAndDeletedFalse(Long clubId);

	boolean existsByMember_MemberIdAndClub_ClubId(Long memberId, Long clubId);

	Optional<ClubMemberEntity> findByClubMemberId(Long clubMemberId);

	List<ClubMemberEntity> findAllByDeletedFalseAndClub_ClubId(Long clubId);
}

