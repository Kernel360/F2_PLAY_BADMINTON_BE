package org.badminton.domain.clubmember.repository;

import org.badminton.domain.clubmember.entity.BannedClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedClubMemberRepository extends JpaRepository<BannedClubMemberEntity,Long> {
}
