package org.badminton.api.service.match;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.badminton.api.interfaces.match.dto.MatchResultResponse;
import org.badminton.domain.domain.match.entity.DoublesMatchEntity;
import org.badminton.domain.domain.match.entity.SinglesMatchEntity;
import org.badminton.domain.infrastructures.match.repository.DoublesMatchRepository;
import org.badminton.domain.infrastructures.match.repository.SinglesMatchRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchResultService {

    private final SinglesMatchRepository singlesMatchRepository;
    private final DoublesMatchRepository doublesMatchRepository;

    public List<MatchResultResponse> getAllMatchResultsByClubMember(Long clubMemberId) {
        List<MatchResultResponse> allResults = new ArrayList<>();

        // 단식 경기 결과 가져오기
        List<SinglesMatchEntity> singlesMatches = singlesMatchRepository.findAllCompletedByClubMemberId(clubMemberId);
        for (SinglesMatchEntity match : singlesMatches) {
            allResults.add(MatchResultResponse.fromSinglesMatchEntity(match, clubMemberId));
        }

        // 복식 경기 결과 가져오기
        List<DoublesMatchEntity> doublesMatches = doublesMatchRepository.findAllCompletedByClubMemberId(clubMemberId);
        for (DoublesMatchEntity match : doublesMatches) {
            allResults.add(MatchResultResponse.fromDoublesMatchEntity(match, clubMemberId));
        }

        allResults.sort((r1, r2) -> r2.leagueAt().compareTo(r1.leagueAt()));
        return allResults;
    }
}
