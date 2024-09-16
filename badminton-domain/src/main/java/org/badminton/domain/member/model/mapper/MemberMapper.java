package org.badminton.domain.member.model.mapper;

import org.badminton.domain.member.model.dto.MemberDto;
import org.badminton.domain.member.model.entity.MemberEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberMapper {

	public MemberEntity toEntity(MemberDto memberDto) {
		return new MemberEntity(memberDto.getEmail(), memberDto.getName(), memberDto.getProviderId(),
			memberDto.getRole());
	}

	public MemberDto toDto(MemberEntity memberEntity) {
		return new MemberDto(memberEntity.getRole(), memberEntity.getName(), memberEntity.getEmail(),
			memberEntity.getProviderId());
	}
}
