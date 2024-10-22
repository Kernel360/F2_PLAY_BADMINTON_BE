package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.api.interfaces.clubmember.validation.ReasonValidator;

public record ClubMemberExpelRequest(
        @ReasonValidator
        String expelReason
) {
}
