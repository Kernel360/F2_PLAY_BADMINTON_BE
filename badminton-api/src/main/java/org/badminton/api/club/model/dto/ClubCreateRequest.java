package org.badminton.api.club.model.dto;

import org.badminton.api.club.validation.ClubNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record ClubCreateRequest(

	@ClubNameValid
	String clubName,

	@NotBlank
	@Size(max = 1000, message = "동호회 소개란에는 최대 1000자까지 입력할 수 있습니다.")
	String clubDescription,

	// TODO: validation 추가 또는 VO 추가
	String clubImage
) {
}