package org.badminton.api.interfaces.match.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badminton.api.interfaces.match.dto.MatchDetailsResponse;
import org.badminton.api.interfaces.match.dto.MatchResponse;
import org.badminton.api.interfaces.match.dto.SetScoreResponse;
import org.badminton.api.interfaces.match.dto.SetScoreUpdateRequest;
import org.badminton.api.interfaces.match.dto.SetScoreUpdateResponse;
import org.badminton.domain.domain.match.command.MatchCommand.UpdateSetScore;
import org.badminton.domain.domain.match.info.MatchInfo;
import org.badminton.domain.domain.match.info.SetInfo;
import org.badminton.domain.domain.match.service.MatchInitService;
import org.badminton.domain.domain.match.service.MatchProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/clubs/{clubId}/leagues/{leagueId}/matches")
public class MatchController {

    private final MatchInitService matchInitService;
    private final MatchProgressService matchProgressService;

    @GetMapping
    @Operation(summary = "대진표 조회",
            description = "대진표를 조회합니다.",
            tags = {"Match"})
    public ResponseEntity<List<MatchResponse>> getAllMatches(
            @PathVariable Long clubId,
            @PathVariable Long leagueId
    ) {
        List<MatchInfo.Main> matchInfo = matchInitService.getAllMatchesInLeague(clubId, leagueId);
        List<MatchResponse> matchResponseList = matchInfo.stream()
                .map(MatchResponse::fromMatchInfo)
                .toList();
        return ResponseEntity.ok(matchResponseList);
    }

    @GetMapping("/{matchId}")
    @Operation(summary = "특정 게임의 세트별 점수 상세 조회",
            description = "특정 게임의 세트별 점수를 상세 조회합니다.",
            tags = {"Match"})
    public ResponseEntity<MatchDetailsResponse> getMatchDetails(
            @PathVariable Long clubId,
            @PathVariable Long leagueId,
            @PathVariable Long matchId
    ) {
        MatchInfo.SetScoreDetails matchDetailsInLeague = matchInitService.getMatchDetailsInLeague(clubId, leagueId,
                matchId);
        return ResponseEntity.ok(MatchDetailsResponse.fromMatchDetailsInfo(matchDetailsInLeague));
    }

    @PostMapping
    @Operation(summary = "대진표 생성",
            description = "대진표를 생성합니다.",
            tags = {"Match"})
    public ResponseEntity<List<MatchResponse>> makeMatches(
            @PathVariable Long clubId,
            @PathVariable Long leagueId
    ) {
        List<MatchInfo.Main> matchInfoList = matchInitService.makeMatches(clubId, leagueId);
        List<MatchResponse> matchResponseList = matchInfoList.stream()
                .map(MatchResponse::fromMatchInfo)
                .toList();
        return ResponseEntity.ok(matchResponseList);
    }

    @GetMapping("/sets")
    @Operation(summary = "모든 게임의 세트 점수 상세 조회",
            description = "모든 게임의 세트 점수를 상세 조회합니다. 모든 게임의 세트별 점수를 조회할 수 있습니다.",
            tags = {"Match"})
    public ResponseEntity<List<SetScoreResponse>> getAllMatchesDetails(
            @PathVariable Long clubId,
            @PathVariable Long leagueId
    ) {

        List<SetInfo.Main> allSetsScoreInLeague = matchInitService.getAllSetsScoreInLeague(clubId,
                leagueId);
        List<SetScoreResponse> setScoreResponseList = allSetsScoreInLeague.stream()
                .map(SetScoreResponse::fromSetInfo)
                .toList();
        return ResponseEntity.ok(setScoreResponseList);
    }

    @PostMapping("/{matchId}/sets/{setIndex}")
    @Operation(summary = "세트별 점수 저장",
            tags = {"Match"})
    public ResponseEntity<SetScoreUpdateResponse> updateSetsScore(
            @PathVariable Long clubId,
            @PathVariable Long leagueId,
            @PathVariable Long matchId,
            @PathVariable int setIndex,
            @Valid @RequestBody SetScoreUpdateRequest setScoreUpdateRequest
    ) {
        UpdateSetScore updateSetScoreCommand = UpdateSetScore.builder()
                .score1(setScoreUpdateRequest.score1())
                .score2(setScoreUpdateRequest.score2())
                .build();

        SetInfo.Main updateSetScoreInfo = matchProgressService.updateSetScore(clubId, leagueId, matchId,
                setIndex, updateSetScoreCommand);
        return ResponseEntity.ok(SetScoreUpdateResponse.fromUpdateSetScoreInfo(updateSetScoreInfo));
    }
}
