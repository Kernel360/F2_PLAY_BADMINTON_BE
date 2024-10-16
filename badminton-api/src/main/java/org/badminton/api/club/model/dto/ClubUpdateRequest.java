package org.badminton.api.club.model.dto;

import org.badminton.api.club.validation.ClubDescriptionValidator;
import org.badminton.api.club.validation.ClubImageValidator;
import org.badminton.api.club.validation.ClubNameValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClubUpdateRequest(
	@ClubNameValidator
	String clubName,

	@ClubDescriptionValidator
	String clubDescription,

	@ClubImageValidator
	String clubImage
) {

}
