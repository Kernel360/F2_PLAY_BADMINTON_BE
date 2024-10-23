package org.badminton.api.interfaces.club.dto;

import org.badminton.api.interfaces.club.validation.ClubDescriptionValidator;
import org.badminton.api.interfaces.club.validation.ClubImageValidator;
import org.badminton.api.interfaces.club.validation.ClubNameValidator;

public record ClubUpdateRequest(
        @ClubNameValidator
        String clubName,

        @ClubDescriptionValidator
        String clubDescription,

        @ClubImageValidator
        String clubImage
) {

}
