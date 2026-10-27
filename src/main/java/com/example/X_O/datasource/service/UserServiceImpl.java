package com.example.X_O.datasource.service;

import com.example.X_O.datasource.mapper.UserMapper;
import com.example.X_O.datasource.repository.UserRepository;
import com.example.X_O.domain.model.Role;
import com.example.X_O.domain.model.User;
import com.example.X_O.domain.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<User> findById(UUID id) {
		if (id == null) {
			return Optional.empty();
		}
		return userRepository.findById(id).map(UserMapper::toDomain);
	}

	@Override
	public Optional<User> findByLogin(String login) {
		if (login == null || login.isBlank()) {
			return Optional.empty();
		}
		return userRepository.findByLogin(login).map(UserMapper::toDomain);
	}

	@Override
	public boolean register(String login, String rawPassword) {
		if (login == null || login.isBlank() || rawPassword == null || rawPassword.isBlank()) {
			return false;
		}
		if (userRepository.findByLogin(login).isPresent()) {
			return false;
		}
		User user = new User(
				UUID.randomUUID(),
				login,
				passwordEncoder.encode(rawPassword),
				List.of(Role.USER));
		try {
			userRepository.save(UserMapper.toEntity(user));
			return true;
		} catch (DataIntegrityViolationException ex) {
			return false;
		}
	}

	@Override
	public Optional<User> authenticate(String login, String rawPassword) {
		if (login == null || rawPassword == null) {
			return Optional.empty();
		}
		return findByLogin(login)
				.filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
	}
}
