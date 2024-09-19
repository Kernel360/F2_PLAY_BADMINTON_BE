package org.badminton.api.member.model.dto;

import java.util.Map;

public class KakaoResponse implements OAuthResponse {

	private final Map<String, Object> attributes;
	private final Map<String, Object> kakaoAccount;

	public KakaoResponse(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		return kakaoAccount.get("email").toString();
	}

	@Override
	public String getName() {
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
		return profile.get("nickname").toString();
	}
}
