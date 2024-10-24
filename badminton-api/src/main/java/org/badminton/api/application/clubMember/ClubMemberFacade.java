package org.badminton.api.application.clubMember;

import org.badminton.domain.domain.clubmember.service.ClubMemberService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubMemberFacade {

	private final ClubMemberService clubMemberService;

	// public ClubMemberJoinInfo joinClub(String memberToken)

}
