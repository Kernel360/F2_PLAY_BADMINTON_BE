package org.badminton.api.member.oauth2.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.badminton.api.member.model.dto.MemberResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

	private final MemberResponse memberResponse;
	
	@Getter
	private final String registrationId;

	private String clubRole;

	public void updateClubRole(String clubRole) {
		this.clubRole = clubRole;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(memberResponse.authorization()));
		if (clubRole != null && !clubRole.isEmpty()) {
			authorities.add(new SimpleGrantedAuthority(clubRole));
		}
		return authorities;
	}

	@Override
	public String getName() {
		return memberResponse.name();
	}

	public String getProviderId() {
		return memberResponse.providerId();
	}

	public String getAuthorization() {
		return memberResponse.authorization();
	}

	public Long getMemberId() {
		return memberResponse.memberId();
	}

	public String getEmail() {
		return memberResponse.email();
	}

	public String getProfileImage() {
		return memberResponse.profileImage();
	}

}
