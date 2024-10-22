package org.badminton.api.interfaces.league.enums;

import lombok.Getter;

@Getter
public enum EndDateType {
    END_HOUR(23),
    END_MINUTE(59);

    private final Integer description;

    EndDateType(Integer description) {
        this.description = description;
    }
}
