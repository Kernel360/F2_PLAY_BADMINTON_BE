package org.badminton.api.club.validator;

import org.badminton.api.common.error.ErrorCode;
import org.badminton.api.common.exception.DuplicationException;
import org.badminton.domain.club.entity.ClubEntity;
import org.badminton.domain.club.repository.ClubRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClubValidator {

	private final ClubRepository clubRepository;

	public void saveClub(ClubEntity clubEntity) {
		clubRepository.save(clubEntity);
	}

	public void checkIfClubPresent(String clubName) {
		clubRepository.findByClubName(clubName).ifPresent(club -> {
			// 해당 클래스 이름을 반환하면 어디서 에러가 발생 했는지 쉽게 파악할 수 있을 것 같아서 아래와 같이 작성했어요
			throw new DuplicationException(ErrorCode.RESOURCE_ALREADY_EXIST, ClubValidator.class.getSimpleName());
		});
	}

}
