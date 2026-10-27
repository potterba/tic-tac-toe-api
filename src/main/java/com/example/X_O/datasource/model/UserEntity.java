package com.example.X_O.datasource.model;

import com.example.X_O.domain.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(nullable = false, unique = true, length = 64)
	private String login;

	@Column(nullable = false, length = 100)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 32)
	private List<Role> roles = new ArrayList<>();

	protected UserEntity() {
	}

	public UserEntity(UUID id, String login, String password, List<Role> roles) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.roles = roles == null ? new ArrayList<>() : new ArrayList<>(roles);
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
		return List.copyOf(roles);
	}
}
