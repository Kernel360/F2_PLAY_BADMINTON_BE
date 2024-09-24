package org.badminton.api.clubmember.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubMemberParticipateRequest(
	@Schema(description = "회원 id", example = "1")
	Long memberId,
	@Schema(description = "동호회 id", example = "1")
	Long clubId
) {

}
