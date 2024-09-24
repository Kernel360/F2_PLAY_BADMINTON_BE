package org.badminton.api.member.oauth2.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.badminton.api.member.model.dto.MemberResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

	private final MemberResponse memberResponse;
	@Getter
	private final String accessToken;
	@Getter
	private final String registrationId;

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return memberResponse.authorization();
			}
		});
		return collection;
	}

	@Override
	public String getName() {
		return memberResponse.name();
	}

	public String getProviderId() {
		return memberResponse.providerId();
	}

	public String getEmail() {
		return memberResponse.email();
	}

	public String getProfileImage() {
		return memberResponse.profileImage();
	}

}
