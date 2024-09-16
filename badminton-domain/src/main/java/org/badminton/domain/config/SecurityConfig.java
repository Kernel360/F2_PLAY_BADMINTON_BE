package org.badminton.domain.config;

import java.util.Arrays;
import java.util.Collections;

import org.badminton.domain.member.jwt.JwtFilter;
import org.badminton.domain.member.jwt.JwtUtil;
import org.badminton.domain.member.oauth2.CustomSuccessHandler;
import org.badminton.domain.member.service.CustomOAuth2MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "org.badminton.domain")
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2MemberService customOAuth2MemberService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JwtUtil jwtUtil;

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
					configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
					configuration.setAllowCredentials(true);
					configuration.setMaxAge(3600L);
					return configuration;
				}
			}));

		http.csrf(csrf -> csrf.disable());
		http.formLogin(formLogin -> formLogin.disable());
		http.httpBasic(httpBasic -> httpBasic.disable());
		http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		http.oauth2Login(oauth2 -> oauth2
			.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2MemberService))
			.successHandler(customSuccessHandler));

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/", "/groups", "/oauth2/**", "/login/**", "/error").permitAll()
			.anyRequest().authenticated());

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

}

