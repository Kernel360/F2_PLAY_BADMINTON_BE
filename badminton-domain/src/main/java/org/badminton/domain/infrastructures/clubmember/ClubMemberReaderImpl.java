package org.badminton.domain.infrastructures.clubmember;

import java.util.List;
import java.util.Objects;

import org.badminton.domain.common.exception.clubmember.ClubMemberAlreadyOwnerException;
import org.badminton.domain.domain.clubmember.ClubMemberReader;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClubMemberReaderImpl implements ClubMemberReader {
	public static final int NOT_OWNER_CLUB = 0;
	private final ClubMemberRepository clubMemberRepository;

	@Override
	public boolean checkIsClubMember(Long memberId, Long clubId) {
		return clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId)
			.map(clubMember -> Objects.equals(clubMember.getClub().getClubId(), clubId))
			.orElse(false);
	}

	@Override
	public void checkIsClubOwner(Long memberId) {
		log.info("checkIsClubOwner : {} ", clubMemberRepository.countByMemberIdAndRoleOwner(memberId));
		if (NOT_OWNER_CLUB != clubMemberRepository.countByMemberIdAndRoleOwner(memberId)) {
			throw new ClubMemberAlreadyOwnerException(memberId);
		}
	}

	@Override
	public List<ClubMemberEntity> getAllMember(Long clubId) {
		return clubMemberRepository.findAllByClub_ClubId(clubId);
	}
}
