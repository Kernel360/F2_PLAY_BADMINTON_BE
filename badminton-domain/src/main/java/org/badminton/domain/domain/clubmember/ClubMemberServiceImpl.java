package org.badminton.domain.domain.clubmember;

import java.util.List;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {
	private final ClubMemberReader clubMemberReader;
	private final ClubMemberStore clubMemberStore;

	@Override
	public boolean checkIfMemberBelongsToClub(Long memberId, Long clubId) {
		return clubMemberReader.checkIsClubMember(memberId, clubId);
	}

	@Override
	public void checkMyOwnClub(Long memberId) {
		clubMemberReader.checkIsClubOwner(memberId);
	}

	@Override
	public ClubMemberEntity createClubMember(Club club, MemberEntity member, ClubMemberRole role) {
		return clubMemberStore.createClubMember(club, member, role);
	}

	@Override
	public void deleteAllClubMembers(Long clubId) {
		List<ClubMemberEntity> clubMembers = clubMemberReader.getAllMember(clubId);
		clubMembers.forEach(clubMember -> {
			clubMember.deleteClubMember();
			clubMemberStore.store(clubMember);
		});
	}
}
