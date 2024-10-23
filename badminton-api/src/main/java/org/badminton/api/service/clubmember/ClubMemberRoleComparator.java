package org.badminton.api.service.clubmember;

import java.util.Comparator;

import org.badminton.domain.domain.clubmember.entity.ClubMemberRole;

public class ClubMemberRoleComparator implements Comparator<ClubMemberRole> {
    @Override
    public int compare(ClubMemberRole o1, ClubMemberRole o2) {
        if (o1 == ClubMemberRole.ROLE_OWNER) {
            return -1;
        }
        if (o2 == ClubMemberRole.ROLE_OWNER) {
            return 1;
        }
        if (o1 == ClubMemberRole.ROLE_MANAGER) {
            return -1;
        }
        if (o2 == ClubMemberRole.ROLE_MANAGER) {
            return 1;
        }
        return 0;
    }
}
