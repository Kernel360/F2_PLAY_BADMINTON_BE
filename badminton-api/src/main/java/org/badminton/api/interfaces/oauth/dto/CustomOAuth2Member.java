package org.badminton.api.interfaces.oauth.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.badminton.api.interfaces.member.dto.MemberResponse;
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

    private final String oAuthAccessToken;

    public String getOAuthAccessToken() {
        return this.oAuthAccessToken;
    }

    private Map<Long, String> clubRoles = new HashMap<>();

    public void addClubRole(Long clubId, String role) {
        this.clubRoles.clear();
        this.clubRoles.put(clubId, role);
    }

    public String getClubRole(Long clubId) {
        return this.clubRoles.get(clubId);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberResponse.getAuthorization()));
        for (Map.Entry<Long, String> entry : clubRoles.entrySet()) {
            String role = entry.getValue().startsWith("ROLE_") ? entry.getValue() : "ROLE_" + entry.getValue();
            authorities.add(new SimpleGrantedAuthority(entry.getKey() + ":" + role));
        }
        return authorities;
    }

    @Override
    public String getName() {
        return memberResponse.getName();
    }

    public String getProviderId() {
        return memberResponse.getProviderId();
    }

    public String getAuthorization() {
        return memberResponse.getAuthorization();
    }

    public Long getMemberId() {
        return memberResponse.getMemberId();
    }

    public String getEmail() {
        return memberResponse.getEmail();
    }

    public String getProfileImage() {
        return memberResponse.getProfileImage();
    }

}
