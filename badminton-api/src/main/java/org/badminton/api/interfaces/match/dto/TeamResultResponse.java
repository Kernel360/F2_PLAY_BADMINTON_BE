package org.badminton.api.interfaces.match.dto;

import org.badminton.domain.domain.league.vo.Team;

public record TeamResultResponse(
        String participant1Name,
        String participant2Name
) {
    public static TeamResultResponse fromTeam(Team team, Long currentClubMemberId) {
        String participant1Name = team.getLeagueParticipant1().getClubMember().getMember().getName();
        String participant2Name = team.getLeagueParticipant2().getClubMember().getMember().getName();

        if (currentClubMemberId != null) {
            return getTeamResultResponse(team, currentClubMemberId, participant1Name, participant2Name);
        }
        return new TeamResultResponse(participant1Name, participant2Name);
    }

    private static TeamResultResponse getTeamResultResponse(Team team, Long currentClubMemberId,
                                                            String participant1Name, String participant2Name) {
        if (team.getLeagueParticipant1().getClubMember().getClubMemberId().equals(currentClubMemberId)) {
            return new TeamResultResponse(participant1Name, participant2Name);
        } else {
            return new TeamResultResponse(participant2Name, participant1Name);
        }
    }
}