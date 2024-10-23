package org.badminton.domain.infrastructures.clubmember;

import org.badminton.domain.domain.clubmember.entity.ClubMemberBanRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedClubMemberRepository extends JpaRepository<ClubMemberBanRecordEntity, Long> {
}
