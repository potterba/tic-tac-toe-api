package com.example.X_O.web.model;

import com.example.X_O.domain.model.Role;

import java.util.List;
import java.util.UUID;

public class UserDto {

	private UUID id;
	private String login;
	private List<Role> roles;

	public UserDto() {
	}

	public UserDto(UUID id, String login, List<Role> roles) {
		this.id = id;
		this.login = login;
		this.roles = roles;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
