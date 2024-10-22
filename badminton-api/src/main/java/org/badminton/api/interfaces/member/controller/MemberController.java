package org.badminton.api.interfaces.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badminton.api.application.member.MemberService;
import org.badminton.api.aws.s3.model.dto.ImageUploadRequest;
import org.badminton.api.aws.s3.service.MemberProfileImageService;
import org.badminton.api.interfaces.match.dto.MatchResultResponse;
import org.badminton.api.interfaces.member.dto.MemberDeleteResponse;
import org.badminton.api.interfaces.member.dto.MemberIsClubMemberResponse;
import org.badminton.api.interfaces.member.dto.MemberMyPageResponse;
import org.badminton.api.interfaces.member.dto.MemberResponse;
import org.badminton.api.interfaces.member.dto.MemberUpdateRequest;
import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
import org.badminton.api.service.leaguerecord.LeagueRecordService;
import org.badminton.api.service.match.MatchResultService;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;
import org.badminton.infra.clubmember.ClubMemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ClubMemberRepository clubMemberRepository;
    private final LeagueRecordService leagueRecordService;
    private final MemberProfileImageService memberProfileImageService;
    private final MatchResultService matchResultService;

    @GetMapping("/matchesRecord")
    @Operation(summary = "동호회 회원 경기 조회",
            description = "동호회 회원 경기 조회.",
            tags = {"Member"})
    public ResponseEntity<List<MatchResultResponse>> readMemberLeagueRecord(
            @AuthenticationPrincipal CustomOAuth2Member member
    ) {
        ClubMemberEntity clubMemberEntity = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(
                member.getMemberId()).orElse(null);
        if (clubMemberEntity == null) {
            return ResponseEntity.ok(null);
        }
        Long clubMemberId = clubMemberEntity.getClubMemberId();
        return ResponseEntity.ok(matchResultService.getAllMatchResultsByClubMember(clubMemberId));
    }

    //TODO: 티어 정상 작동 테스트용, 지울예정입니다
    @Operation(
            summary = "승리 기록 추가",
            description = "회원의 리그 기록에 승리를 추가합니다",
            tags = {"LeagueRecord"}
    )
    @PostMapping("/win")
    public ResponseEntity<String> addWin(@AuthenticationPrincipal CustomOAuth2Member member) {
        Long memberId = member.getMemberId();
        Long clubMemberId = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId).get().getClubMemberId();
        leagueRecordService.addWin(clubMemberId);
        return ResponseEntity.ok("Win added successfully");
    }

    //TODO: 티어 정상 작동 테스트용, 지울예정입니다
    @Operation(
            summary = "패배 기록 추가",
            description = "회원의 리그 기록에 패배를 추가합니다",
            tags = {"LeagueRecord"}
    )
    @PostMapping("/lose")
    public ResponseEntity<String> addLose(@AuthenticationPrincipal CustomOAuth2Member member) {
        Long memberId = member.getMemberId();
        Long clubMemberId = clubMemberRepository.findByMember_MemberIdAndDeletedFalse(memberId).get().getClubMemberId();
        leagueRecordService.addLose(clubMemberId);
        return ResponseEntity.ok("Loss added successfully");
    }

    @Operation(
            summary = "액세스 토큰을 재발급합니다",
            description = "리프레시 토큰을 이용해서 액세스 토큰을 재발급합니다",
            tags = {"Member"}
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return memberService.refreshToken(request, response);
    }

    @Operation(
            summary = "로그아웃을 합니다",
            description = "쿠키에서 JWT 토큰을 제거해 로그아웃을 합니다",
            tags = {"Member"}
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response,
                                         @AuthenticationPrincipal CustomOAuth2Member member) {
        log.info("Logout request received");
        memberService.logoutMember(member.getMemberId(), response);
        return ResponseEntity.ok("Logged out successfully");
    }

    @Operation(
            summary = "프로필 사진을 S3에 업로드 합니다",
            description = "프로필 사진을 S3에 업로드합니다",
            tags = {"Member"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = ImageUploadRequest.class)
                    )
            )
    )
    @PostMapping("/profileImage")
    public ResponseEntity<String> uploadProfileImage(
            @RequestPart(value = "multipartFile") MultipartFile multipartFile,
            @AuthenticationPrincipal CustomOAuth2Member member) {
        ImageUploadRequest request = new ImageUploadRequest(multipartFile);
        return ResponseEntity.ok(memberProfileImageService.uploadFile(request, member.getMemberId()));
    }

    @Operation(
            summary = "프로필 사진을 수정합니다",
            description = """
                    프로필 사진을 수정합니다. 다음 조건을 만족해야 합니다:
                    			
                    1. 프로필 이미지 URL:
                       - 호스트: badminton-team.s3.ap-northeast-2.amazonaws.com
                       - 경로: /member-profile/로 시작
                       - 파일 확장자: png, jpg, jpeg, gif 중 하나""",
            tags = {"Member"}
    )
    @PutMapping("/profileImage")
    public ResponseEntity<MemberResponse> updateProfileImage(
            @Valid @RequestBody MemberUpdateRequest request,
            @AuthenticationPrincipal CustomOAuth2Member member) {
        return ResponseEntity.ok(memberService.updateProfileImage(member.getMemberId(), request.profileImageUrl()));
    }


    @Operation(
            summary = "회원 탈퇴를 합니다",
            description = "멤버 필드의 isDeleted 를 true 로 변경합니다",
            tags = {"Member"}
    )
    @DeleteMapping
    public ResponseEntity<MemberDeleteResponse> deleteMember(HttpServletRequest request, HttpServletResponse response,
                                                             @AuthenticationPrincipal CustomOAuth2Member member) {
        MemberDeleteResponse deleteResponse = memberService.deleteMember(member, request, response);
        return ResponseEntity.ok(deleteResponse);
    }

    @Operation(
            summary = "회원 정보를 조회합니다",
            description = "회원의 마이페이지 접근 시 정보 조회 (동호회 정보 포함)",
            tags = {"Member"}
    )
    @GetMapping("/myPage")
    public ResponseEntity<MemberMyPageResponse> getMemberInfo(@AuthenticationPrincipal CustomOAuth2Member member) {
        return ResponseEntity.ok(memberService.getMemberInfo(member.getMemberId()));
    }

    @Operation(
            summary = "회원이 동호회에 가입되어있는지 확인합니다",
            description = "회원이 동호회에 가입되어있는지 확인합니다",
            tags = {"Member"}
    )
    @GetMapping("/is-club-member")
    public ResponseEntity<MemberIsClubMemberResponse> getMemberIsClubMember(
            @AuthenticationPrincipal CustomOAuth2Member member) {
        Long memberId = member.getMemberId();
        return ResponseEntity.ok(memberService.getMemberIsClubMember(memberId));
    }

}

