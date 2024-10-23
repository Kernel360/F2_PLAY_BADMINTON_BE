package org.badminton.api.interfaces.clubmember.dto;

import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;

public record ClubMemberRoleUpdateRequest(
        ClubMemberRole role
) {

}
