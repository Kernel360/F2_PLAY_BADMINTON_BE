package org.badminton.api.club.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ClubAddRequest {
	private String clubName;
	private String clubDescription;
	private String clubImage;
}
