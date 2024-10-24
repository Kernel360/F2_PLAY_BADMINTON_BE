package org.badminton.domain.infrastructures.clubmember;

import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.club.info.ClubCreateInfo;
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
	public ClubMemberEntity createClubMember(ClubCreateInfo clubCreateInfo, MemberEntity member, ClubMemberRole role) {
		var club = new Club(clubCreateInfo);
		var clubMember = new ClubMemberEntity(club, member, role);
		return clubMemberRepository.save(clubMember);
	}

	@Override
	public void store(ClubMemberEntity clubMember) {
		clubMemberRepository.save(clubMember);
	}
}
