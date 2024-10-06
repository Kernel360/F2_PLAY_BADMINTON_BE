package org.badminton.api.config.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.member.jwt.JwtFilter;
import org.badminton.api.member.jwt.JwtUtil;
import org.badminton.api.member.oauth2.CustomSuccessHandler;
import org.badminton.api.member.service.CustomOAuth2MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final CustomOAuth2MemberService customOAuth2MemberService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JwtUtil jwtUtil;
	private final ClubMemberService clubMemberService;
	private final ClubPermissionEvaluator clubPermissionEvaluator;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration configuration = new CorsConfiguration();
					configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
					configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
					configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
					configuration.setAllowCredentials(true);
					configuration.setMaxAge(3600L);
					return configuration;
				}
			}));

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.addFilterBefore(new JwtFilter(jwtUtil, clubMemberService), UsernamePasswordAuthenticationFilter.class);

		http
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(
					userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2MemberService))
				.successHandler(customSuccessHandler));

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/groups", "/oauth2/**", "/login/**", "/error", "/api/*", "/swagger-ui/**",
					"/v3/api-docs/**")
				.permitAll()
				.requestMatchers(HttpMethod.DELETE, "/v1/club")
				.access(hasClubRole("OWNER"))
				.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.PATCH, "/v1/club", "/v1/club/{clubId}/league/{leagueId}")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league/{leagueId}/participation")
				.access(hasClubRole("OWNER", "MANAGER", "USER"))
				.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}/participation")
				.access(hasClubRole("OWNER", "MANAGER", "USER"))
				.requestMatchers(HttpMethod.GET, "/v1/club/{clubId}/league/{leagueId}/participation")
				.access(hasClubRole("OWNER", "MANAGER", "USER"))
				.anyRequest()
				.authenticated());

		http
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	private AuthorizationManager<RequestAuthorizationContext> hasClubRole(String... roles) {
		return (authentication, context) -> {
			Authentication auth = authentication.get();
			if (auth == null || !auth.isAuthenticated()) {
				return new AuthorizationDecision(false);
			}

			String clubId = getClubIdFromContext(context);
			if (clubId == null) {
				return new AuthorizationDecision(false);
			}

			boolean hasRole = clubPermissionEvaluator.hasClubRole(auth, Long.parseLong(clubId), roles);

			log.info("""
				  Checking roles for clubId: {}
				  User authorities: {}
				  Required roles: {}
				  Has required role: {}
				""", clubId, auth.getAuthorities(), Arrays.toString(roles), hasRole
			);

			return new AuthorizationDecision(hasRole);
		};
	}

	private String getClubIdFromContext(RequestAuthorizationContext context) {
		String clubId = context.getVariables().get("clubId");
		if (clubId != null) {
			return clubId;
		}
		HttpServletRequest request = context.getRequest();
		return Optional.ofNullable(request.getParameter("clubId"))
			.filter(s -> !s.isEmpty())
			.orElse(null);
	}

}

