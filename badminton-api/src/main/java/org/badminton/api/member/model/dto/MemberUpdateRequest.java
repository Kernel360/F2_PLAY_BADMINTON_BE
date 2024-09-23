package org.badminton.api.member.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "회원 수정 DTO")
@Getter
public class MemberUpdateRequest {
	
	@Schema(description = "프로필 사진", example = "http://img1.kakaocdn.net/thumb/R640x640.q70/?fname=http://t1.kakaocdn.net/account_images/default_profile.jpeg")
	String profileImage;

}
