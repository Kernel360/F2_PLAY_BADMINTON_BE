// package org.badminton.api.interfaces.clubmember.controller;
//
// import java.util.List;
// import java.util.Map;
//
// import org.badminton.api.common.response.CommonResponse;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRecordResponse;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberBanRequest;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberExpelRequest;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberJoinResponse;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberResponse;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberRoleUpdateRequest;
// import org.badminton.api.interfaces.clubmember.dto.ClubMemberWithdrawResponse;
// import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
// import org.badminton.domain.domain.clubmember.entity.ClubMember;
// import org.badminton.domain.domain.clubmember.info.ClubMemberInfo;
// import org.badminton.domain.domain.clubmember.service.ClubMemberService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PatchMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @RequiredArgsConstructor
// @RestController
// @Slf4j
// @RequestMapping("/v1/clubs/{clubId}/clubMembers")
// public class ClubMemberController {
// 	private final ClubMemberService clubMemberService;
//
// 	@Operation(summary = "동호회 회원 전체 조회",
// 		description = "동호회에 가입한 회원들의 리스트를 조회합니다.",
// 		tags = {"ClubMember"})
// 	@GetMapping
// 	public CommonResponse<Map<ClubMember.ClubMemberRole, List<ClubMemberInfo>>> getClubMembersInClub(
// 		@PathVariable Long clubId
// 	) {
// 		Map<ClubMember.ClubMemberRole, List<ClubMemberInfo>> clubMemberResponseList =
// 			clubMemberService.findAllClubMembers(clubId);
// 		return CommonResponse.success(clubMemberResponseList);
// 	}
//
// 	@Operation(summary = "동호회 가입 신청",
// 		description = "동호회에 가입을 신청합니다.",
// 		tags = {"ClubMember"})
// 	@PostMapping
// 	public ResponseEntity<ClubMemberJoinResponse> joinClub(@AuthenticationPrincipal CustomOAuth2Member member,
// 		@Parameter(description = "동호회 ID", example = "1") @PathVariable Long clubId) {
//
// 		log.info("member :{}", member);
//
// 		String memberToken = member.getMemberToken();
//
// 		ClubMemberJoinResponse clubMemberJoinResponse = clubMemberService.joinClub(memberToken, clubId);
//
// 		return ResponseEntity.ok(clubMemberJoinResponse);
// 	}
//
// 	@Operation(
// 		summary = "동호회원 역할 변경시키기",
// 		description = """
// 			동호회원의 역할을 변경시킵니다. 다음 제약 사항과 정보를 반드시 확인해야 합니다:
//
// 			1. 회원 역할:
// 			   - 탈퇴 대상 회원의 현재 역할을 나타냅니다.
// 			   - 다음 중 하나여야 합니다:
// 			     * ROLE_MANAGER: 동호회 관리자
// 			     * ROLE_USER: 일반 회원
// 			주의사항:\s
// 			- ROLE_MANAGER(동호회 관리자)를 강제 탈퇴시키려면 ROLE_OWNER 권한이 필요합니다.""",
//
// 		tags = {"ClubMember"}
// 	)
// 	@PatchMapping("/role")
// 	public ResponseEntity<ClubMemberResponse> updateClubMemberRole(
// 		@Valid @RequestBody ClubMemberRoleUpdateRequest request,
// 		@RequestParam Long clubMemberId, @PathVariable Long clubId) {
// 		return ResponseEntity.ok(clubMemberService.updateClubMemberRole(request, clubMemberId));
// 	}
//
// 	@Operation(
// 		summary = "동호회원 강제 탈퇴시키기",
// 		description = """
// 			동호회원을 강제로 탈퇴시킵니다. 다음 제약 사항을 반드시 준수해야 합니다:
//
// 			1. 회원 제제 사유:
// 			   - 필수 입력 항목입니다.
// 			   - 최소 2자 이상이어야 합니다.
// 			   - 최대 100자 이하여야 합니다.
//
// 			""",
// 		tags = {"ClubMember"}
// 	)
// 	@PatchMapping("/expel")
// 	public ResponseEntity<ClubMemberBanRecordResponse> expelClubMember(@RequestParam Long clubMemberId,
// 		@PathVariable Long clubId, @Valid @RequestBody
// 	ClubMemberExpelRequest request) {
// 		return ResponseEntity.ok(clubMemberService.expelClubMember(request, clubMemberId));
// 	}
//
// 	@Operation(
// 		summary = "동호회원 정지시키기",
// 		description = """
// 			동호회원을 정지시킵니다. 다음 제약 사항을 반드시 준수해야 합니다:
//
// 			1. 회원 제제 사유:
// 			   - 필수 입력 항목입니다.
// 			   - 최소 2자 이상이어야 합니다.
// 			   - 최대 100자 이하여야 합니다.
// 			2. 정지 유형:
// 			   - 필수 선택 항목입니다.\\n" +
// 			   - 다음 중 하나를 입력해야 합니다:\\n" +
// 			     THREE_DAYS: 3일 정지
// 			     SEVEN_DAYS: 7일 정지
// 			     TWO_WEEKS: 14일 정지
//
// 			""",
// 		tags = {"ClubMember"}
// 	)
// 	@PatchMapping("/ban")
// 	public ResponseEntity<ClubMemberBanRecordResponse> banClubMember(@RequestParam Long clubMemberId,
// 		@PathVariable Long clubId,
// 		@Valid @RequestBody ClubMemberBanRequest request) {
// 		return ResponseEntity.ok(clubMemberService.banClubMember(request, clubMemberId));
// 	}
//
// 	@Operation(
// 		summary = "동호회에서 탈퇴하기",
// 		description = "동호회에서 탈퇴하기",
// 		tags = {"ClubMember"}
// 	)
// 	@DeleteMapping
// 	public ResponseEntity<ClubMemberWithdrawResponse> withdrawMember(@AuthenticationPrincipal CustomOAuth2Member member,
// 		@PathVariable Long clubId) {
// 		return ResponseEntity.ok(clubMemberService.withDrawClubMember(clubId, member.getMemberId()));
// 	}
//
// }
//
