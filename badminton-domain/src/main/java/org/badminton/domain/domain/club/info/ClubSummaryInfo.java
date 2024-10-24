package org.badminton.domain.domain.club.info;

import org.badminton.domain.common.enums.MemberTier;
import org.badminton.domain.domain.club.Club;
import org.badminton.domain.domain.clubmember.entity.ClubMemberEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ClubSummaryInfo(
        Long clubId,
        String clubName,
        String clubDescription,
        String clubImage,
        LocalDateTime createdAt,
        List<ClubMemberEntity> clubMembers
        ) {

    public static ClubSummaryInfo toClubSummaryInfo(Club club) {
        return new ClubSummaryInfo(
                club.getClubId(),
                club.getClubName(),
                club.getClubDescription(),
                club.getClubImage(),
                club.getCreatedAt(),
                club.getClubMembers()
        );
    }
    public Map<MemberTier, Long> getClubMemberCountByTier() {

        return getMemberTierLongMap(clubMembers);
    }

    public static Map<MemberTier, Long> getMemberTierLongMap(List<ClubMemberEntity> clubMembers) {
        List<MemberTier> tierListForInit = Arrays.asList(MemberTier.BRONZE, MemberTier.SILVER, MemberTier.GOLD);
        Map<MemberTier, Long> tierCounts = new LinkedHashMap<>();

        for (MemberTier tier : tierListForInit) {
            tierCounts.put(tier, 0L);
        }

        Map<MemberTier, Long> actualCounts = clubMembers.stream()
                .filter(clubMember -> clubMember.getLeagueRecord() != null)
                .filter(clubMember -> !clubMember.isDeleted())
                .collect(Collectors.groupingBy(
                        ClubMemberEntity::getTier,
                        Collectors.counting()
                ));

        tierCounts.putAll(actualCounts);

        return tierCounts;
    }
}
