package com.example.X_O.domain.service;

import com.example.X_O.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

	Optional<User> findByLogin(String login);

	Optional<User> findById(UUID id);

	boolean register(String login, String rawPassword);

	Optional<User> authenticate(String login, String rawPassword);
}
