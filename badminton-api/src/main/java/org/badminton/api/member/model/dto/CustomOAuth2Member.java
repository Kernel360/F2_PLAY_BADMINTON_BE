package org.badminton.api.member.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

	private final MemberResponse memberResponse;

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
}
