// package org.badminton.api.interfaces.league.controller;
//
// import java.util.List;
//
// import org.badminton.api.interfaces.league.dto.LeagueByDateResponse;
// import org.badminton.api.interfaces.league.dto.LeagueCancelResponse;
// import org.badminton.api.interfaces.league.dto.LeagueCreateRequest;
// import org.badminton.api.interfaces.league.dto.LeagueCreateResponse;
// import org.badminton.api.interfaces.league.dto.LeagueDetailsResponse;
// import org.badminton.api.interfaces.league.dto.LeagueReadResponse;
// import org.badminton.api.interfaces.league.dto.LeagueUpdateRequest;
// import org.badminton.api.interfaces.league.dto.LeagueUpdateResponse;
// import org.badminton.api.service.league.LeagueService;
// import org.badminton.api.interfaces.oauth.dto.CustomOAuth2Member;
// import org.springframework.format.annotation.DateTimeFormat;
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
//
// @RequiredArgsConstructor
// @RestController
// @RequestMapping("/v1/clubs/{clubId}/leagues")
// public class LeagueController {
//     private final LeagueService leagueService;
//
//     @Operation(
//             summary = "월별로 경기 일정을 조회합니다.",
//             description = "월별로 경기 일정을 리스트로 조회할 수 있습니다. 날짜는 'yyyy-MM' 형식으로 제공되어야 합니다.",
//             tags = {"league"},
//             parameters = {
//                     @Parameter(name = "clubId", description = "조회할 클럽의 ID", required = true),
//                     @Parameter(name = "date", description = "조회할 날짜, 'yyyy-MM' 형식으로 입력", required = true)
//             }
//     )
//     @GetMapping("/month")
//     public ResponseEntity<List<LeagueReadResponse>> getLeagueByMonth(@PathVariable Long clubId,
//                                                                      @RequestParam
//                                                                      @DateTimeFormat(pattern = "yyyy-MM") String date) {
//         return ResponseEntity.ok(leagueService.getLeaguesByMonth(clubId, date));
//     }
//
//     @Operation(
//             summary = "일자별로 경기 일정을 조회합니다.",
//             description = "일별로 경기 일정을 리스트로 조회할 수 있습니다. 검색 조건으로 날짜를 사용하며, 날짜는 'yyyy-MM' 형식으로 제공되어야 합니다.",
//             tags = {"league"},
//             parameters = {
//                     @Parameter(name = "clubId", description = "조회할 클럽의 ID", required = true),
//                     @Parameter(name = "date", description = "조회할 날짜, 'yyyy-MM-dd' 형식으로 입력", required = true)
//             }
//     )
//
//     @GetMapping("/date")
//     public ResponseEntity<List<LeagueByDateResponse>> getLeagueByDate(@PathVariable Long clubId,
//                                                                       @RequestParam
//                                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
//         return ResponseEntity.ok(leagueService.getLeaguesByDate(clubId, date));
//     }
//
//     @Operation(
//             summary = "경기를 생성합니다.",
//             description = "경기 생성하고를 데이터베이스에 저장합니다.",
//             tags = {"league"}
//     )
//     @PostMapping
//     public ResponseEntity<LeagueCreateResponse> createLeague(
//             @PathVariable Long clubId,
//             @Valid @RequestBody LeagueCreateRequest leagueCreateRequest) {
//         return ResponseEntity.ok(leagueService.createLeague(clubId, leagueCreateRequest));
//     }
//
//     @Operation(
//             summary = "특정 경기를 조회합니다.",
//             description = "특정 경기를 경기 아이디를 통해 데이터베이스에서 조회합니다.",
//             tags = {"league"}
//     )
//     @GetMapping("/{leagueId}")
//     public ResponseEntity<LeagueDetailsResponse> leagueRead(@PathVariable Long clubId, @PathVariable Long leagueId,
//                                                             @AuthenticationPrincipal CustomOAuth2Member member) {
//         return ResponseEntity.ok(leagueService.getLeague(clubId, leagueId, member.getMemberId()));
//     }
//
//     @Operation(
//             summary = "경기의 세부 정보를 변경합니다.",
//             description = "경기 제목, 경기 상태 등을 변경할 수 있습니다.",
//             tags = {"league"}
//     )
//     @PatchMapping("/{leagueId}")
//     public ResponseEntity<LeagueUpdateResponse> updateLeague(
//             @PathVariable Long clubId,
//             @PathVariable Long leagueId,
//             @Valid @RequestBody LeagueUpdateRequest leagueUpdateRequest) {
//         return ResponseEntity.ok(leagueService.updateLeague(clubId, leagueId, leagueUpdateRequest));
//     }
//
//     @Operation(
//             summary = "경기 취소",
//             description = "경기를 취소합니다.",
//             tags = {"league"}
//     )
//     @DeleteMapping("/{leagueId}")
//     public ResponseEntity<LeagueCancelResponse> cancelLeague(
//             @PathVariable Long clubId,
//             @PathVariable Long leagueId) {
//         return ResponseEntity.ok(leagueService.cancelLeague(clubId, leagueId));
//     }
// }
