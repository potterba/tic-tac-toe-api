package com.example.X_O.domain.model;

import java.util.List;
import java.util.UUID;

public final class User {

	private final UUID id;
	private final String login;
	private final String password;
	private final List<Role> roles;

	public User(UUID id, String login, String password, List<Role> roles) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.roles = roles == null ? List.of() : List.copyOf(roles);
	}

	public UUID getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public List<Role> getRoles() {
		return roles;
	}
}
