package org.badminton.domain.clubmember.repository;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.clubmember.entity.ClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMemberEntity, Long> {
	Optional<List<ClubMemberEntity>> findAllByClub_ClubId(Long clubId);

	Optional<ClubMemberEntity> findByMember_MemberId(Long memberId);

}
