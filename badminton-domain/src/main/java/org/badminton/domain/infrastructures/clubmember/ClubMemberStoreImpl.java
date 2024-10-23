package org.badminton.domain.infrastructures.clubmember;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.ClubMemberStore;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;
import org.badminton.domain.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClubMemberStoreImpl implements ClubMemberStore {
	private final ClubMemberRepository clubMemberRepository;

	@Override
	public ClubMemberEntity createClubMember(Club club, MemberEntity member, ClubMemberRole role) {
		var clubMember = new ClubMemberEntity(club, member, role);
		return clubMemberRepository.save(clubMember);
	}

	@Override
	public void store(ClubMemberEntity clubMember) {
		clubMemberRepository.save(clubMember);
	}
}
