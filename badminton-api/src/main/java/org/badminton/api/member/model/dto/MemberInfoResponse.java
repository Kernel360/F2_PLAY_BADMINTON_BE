package org.badminton.api.member.model.dto;

import org.badminton.domain.member.entity.MemberEntity;

public record MemberInfoResponse(
	Long memberId,
	String name,
	String email,
	String profileImage
) implements MemberDetailResponse {
	public static MemberInfoResponse entityToMemberInfoResponse(MemberEntity memberEntity) {
		return new MemberInfoResponse(
			memberEntity.getMemberId(),
			memberEntity.getName(),
			memberEntity.getEmail(),
			memberEntity.getProfileImage()
		);
	}

	@Override
	public Long getMemberId() {
		return memberId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getProfileImage() {
		return profileImage;
	}
}
