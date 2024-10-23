package org.badminton.api.interfaces.member.dto;

import org.badminton.domain.domain.member.entity.MemberEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 삭제 responseDto")
public record MemberDeleteResponse(
        @Schema(description = "멤버 id", example = "1")
        Long memberId,
        @Schema(description = "삭제 여부", example = "true")
        boolean isDeleted
) {

    public static MemberDeleteResponse memberEntityToDeleteResponse(MemberEntity memberEntity) {
        return new MemberDeleteResponse(memberEntity.getMemberId(), memberEntity.isMemberDeleted());
    }

}
