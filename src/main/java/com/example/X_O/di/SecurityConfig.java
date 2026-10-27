package com.example.X_O.di;

import com.example.X_O.service.JwtProvider;
import com.example.X_O.service.JwtUtil;
import com.example.X_O.web.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			JwtProvider jwtProvider,
			JwtUtil jwtUtil) throws Exception {
		AuthFilter authFilter = new AuthFilter(jwtProvider, jwtUtil);
		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/register", "/auth/login", "/auth/token", "/auth/refresh").permitAll()
						.requestMatchers(HttpMethod.GET, "/", "/index.html", "/favicon.ico", "/error").permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(authFilter, AnonymousAuthenticationFilter.class);
		return http.build();
	}
}
