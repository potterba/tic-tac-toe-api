package com.example.X_O.service;

import com.example.X_O.domain.model.JwtAuthentication;
import com.example.X_O.domain.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtUtil {

	public JwtAuthentication createAuthentication(Claims claims) {
		if (claims == null) {
			throw new IllegalArgumentException("JWT claims must not be null");
		}
		String uuid = claims.get("uuid", String.class);
		Object rolesClaim = claims.get("roles");
		if (!(rolesClaim instanceof List<?> values) || values.isEmpty()) {
			throw new IllegalArgumentException("JWT roles claim is missing");
		}
		List<Role> roles = values.stream()
				.map(String::valueOf)
				.map(Role::valueOf)
				.toList();
		return new JwtAuthentication(UUID.fromString(uuid), roles, true);
	}
}
