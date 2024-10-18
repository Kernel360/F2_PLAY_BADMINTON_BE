package org.badminton.api.club.model.dto;

import org.badminton.api.club.validation.ClubDescriptionValidator;
import org.badminton.api.club.validation.ClubImageValidator;
import org.badminton.api.club.validation.ClubNameValidator;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "동호회 생성 요청")
public record ClubCreateRequest(

	@ClubNameValidator
	@Schema(description = "동호회 이름", example = "멋있는 동호회")
	String clubName,

	@ClubDescriptionValidator
	@Schema(description = "동호회 설명", example = "배드민턴을 즐겁게 치는 즐거운 멋진 동호회입니다.")
	String clubDescription,

	@ClubImageValidator
	@Schema(description = "동호회 이미지 URL", example = "https://badminton-team.s3.ap-northeast-2.amazonaws.com/club-banner/920d209c-7c26-4813-b002-7b582e02ce9f/banner.png")
	String clubImage
) {
}