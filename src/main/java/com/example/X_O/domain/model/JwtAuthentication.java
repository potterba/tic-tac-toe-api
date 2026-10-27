package com.example.X_O.domain.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class JwtAuthentication implements Authentication {

	@Serial
	private static final long serialVersionUID = 1L;

	private final UUID userId;
	private final List<Role> roles;
	private boolean authenticated;

	public JwtAuthentication(UUID userId, List<Role> roles, boolean authenticated) {
		this.userId = userId;
		this.roles = roles == null ? List.of() : List.copyOf(roles);
		this.authenticated = authenticated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public UUID getPrincipal() {
		return userId;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

	@Override
	public String getName() {
		return userId.toString();
	}
}
