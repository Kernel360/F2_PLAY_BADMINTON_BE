package org.badminton.domain.infrastructures.clubmember;

import java.util.List;
import java.util.Optional;

import org.badminton.domain.domain.clubmember.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findAllByClub_ClubId(Long clubId);

    Optional<ClubMember> findByDeletedFalseAndMemberMemberToken(String memberToken);

    Optional<ClubMember> findByClub_ClubIdAndMemberMemberToken(Long clubId, String memberToken);

    boolean existsByMember_MemberTokenAndDeletedFalse(String memberToken);

    List<ClubMember> findAllByMemberMemberToken(String memberToken);

    List<ClubMember> findAllByClubClubIdAndBannedFalseAndDeletedFalse(Long clubId);

    boolean existsByMemberMemberTokenAndClubClubId(String MemberToken, Long clubId);

    Optional<ClubMember> findByClubMemberId(Long clubMemberId);

    List<ClubMember> findAllByDeletedFalseAndClub_ClubId(Long clubId);

	@Query("SELECT COUNT(cm) FROM ClubMember cm WHERE cm.member.memberToken = :memberToken AND cm.role = 'ROLE_OWNER'")
	long countByMemberIdAndRoleOwner(@Param("memberToken") String memberToken);
}

