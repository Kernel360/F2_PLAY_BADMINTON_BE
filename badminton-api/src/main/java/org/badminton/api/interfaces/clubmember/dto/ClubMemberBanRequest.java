package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.api.interfaces.clubmember.validation.ReasonValidator;
import org.badminton.domain.domain.clubmember.entity.BannedType;

public record ClubMemberBanRequest(

        BannedType type,

        @ReasonValidator
        String bannedReason
) {
}
