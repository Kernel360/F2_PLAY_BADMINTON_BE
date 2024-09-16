package org.badminton.api.club.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ClubJoinResponse {

	private String name;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

}
