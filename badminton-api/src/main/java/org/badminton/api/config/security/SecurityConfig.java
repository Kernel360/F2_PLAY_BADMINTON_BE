package org.badminton.api.config.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.badminton.api.clubmember.service.ClubMemberService;
import org.badminton.api.filter.ClubMembershipFilter;
import org.badminton.api.filter.ClubRoleAuthorizationFilter;
import org.badminton.api.filter.JwtAuthenticationFilter;
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
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

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

	// OAuth2 로그인 및 공개 경로: 인증 없이 접근 가능하며, OAuth2 로그인 설정 포함
	@Bean
	public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/", "/oauth2/**", "/login/**", "/error",
				"/swagger-ui/**", "/v1/club/all", "/v1/club")
			.csrf(AbstractHttpConfigurer::disable)
			.cors(this::corsConfigurer)
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(
					userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2MemberService))
				.successHandler(customSuccessHandler));
		return http.build();
	}

	// JWT 인증만 필요한 경로
	@Bean
	public SecurityFilterChain jwtOnlyFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/v1/member/**", "/v1/clubMember", "/v1/club/create")
			.csrf(AbstractHttpConfigurer::disable)
			.cors(this::corsConfigurer)
			.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, clubMemberService),
				UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public SecurityFilterChain clubFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/v1/club/{clubId}/league/**", "/v1/club/handle")
			.csrf(AbstractHttpConfigurer::disable)
			.cors(this::corsConfigurer)
			.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, clubMemberService),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(new ClubMembershipFilter(clubMemberService), JwtAuthenticationFilter.class)
			.addFilterAfter(new ClubRoleAuthorizationFilter(clubPermissionEvaluator), ClubMembershipFilter.class)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.DELETE, "/v1/club/handle")
				.access(hasClubRole("OWNER"))
				.requestMatchers(HttpMethod.PATCH, "/v1/club/handle")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.GET, "/v1/club/{clubId}/**")
				.authenticated()
				.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.PATCH, "/v1/club/{clubId}/league/{leagueId}")
				.access(hasClubRole("OWNER", "MANAGER"))
				.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league/{leagueId}/participation")
				.access(hasClubRole("OWNER", "MANAGER", "USER"))
				.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}/participation")
				.access(hasClubRole("OWNER", "MANAGER", "USER"))
				.anyRequest()
				.authenticated());
		return http.build();
	}

	// // 클럽 멤버 권한이 필요한 경로
	// @Bean
	// public SecurityFilterChain clubMemberFilterChain(HttpSecurity http) throws Exception {
	// 	http
	// 		.securityMatcher("/v1/club/{clubId}/**")
	// 		.csrf(AbstractHttpConfigurer::disable)
	// 		.cors(this::corsConfigurer)
	// 		.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, clubMemberService),
	// 			UsernamePasswordAuthenticationFilter.class)
	// 		.addFilterAfter(new ClubMembershipFilter(clubMemberService), JwtAuthenticationFilter.class)
	// 		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	// 		.authorizeHttpRequests(auth -> auth
	// 			.requestMatchers(HttpMethod.GET, "/v1/club/{clubId}/**").authenticated()
	// 			.anyRequest().access(hasClubRole("OWNER", "MANAGER", "USER")));
	// 	return http.build();
	// }
	//
	// @Bean
	// public SecurityFilterChain clubRoleFilterChain(HttpSecurity http) throws Exception {
	// 	http
	// 		.securityMatcher("/v1/club/**", "/v1/club/{clubId}/league/**", "/v1/club/{clubId}/participation/**")
	// 		.csrf(AbstractHttpConfigurer::disable)
	// 		.cors(this::corsConfigurer)
	// 		.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, clubMemberService),
	// 			UsernamePasswordAuthenticationFilter.class)
	// 		.addFilterAfter(new ClubMembershipFilter(clubMemberService), JwtAuthenticationFilter.class)
	// 		.addFilterAfter(new ClubRoleAuthorizationFilter(clubPermissionEvaluator), ClubMembershipFilter.class)
	// 		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	// 		.authorizeHttpRequests(auth -> auth
	// 			.requestMatchers(HttpMethod.DELETE, "/v1/club")
	// 			.access(hasClubRole("OWNER"))
	// 			.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league")
	// 			.access(hasClubRole("OWNER", "MANAGER"))
	// 			.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}")
	// 			.access(hasClubRole("OWNER", "MANAGER"))
	// 			.requestMatchers(HttpMethod.PATCH, "/v1/club/{clubId}/league/{leagueId}")
	// 			.access(hasClubRole("OWNER", "MANAGER"))
	// 			.requestMatchers(HttpMethod.POST, "/v1/club/{clubId}/league/{leagueId}/participation")
	// 			.access(hasClubRole("OWNER", "MANAGER", "USER"))
	// 			.requestMatchers(HttpMethod.DELETE, "/v1/club/{clubId}/league/{leagueId}/participation")
	// 			.access(hasClubRole("OWNER", "MANAGER", "USER"))
	// 			.requestMatchers(HttpMethod.GET, "/v1/club/{clubId}/league/{leagueId}/participation")
	// 			.access(hasClubRole("OWNER", "MANAGER", "USER"))
	// 			.anyRequest()
	// 			.authenticated());
	// 	return http.build();
	// }

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

	private void corsConfigurer(CorsConfigurer<HttpSecurity> corsConfigurer) {
		corsConfigurer.configurationSource(request -> {
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
			configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
			configuration.setAllowCredentials(true);
			configuration.setMaxAge(3600L);
			return configuration;
		});
	}
}
