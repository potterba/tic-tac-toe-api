package com.example.X_O.service;

import com.example.X_O.domain.model.JwtAuthentication;
import com.example.X_O.domain.model.User;
import com.example.X_O.domain.service.UserService;
import com.example.X_O.web.model.JwtRequest;
import com.example.X_O.web.model.JwtResponse;
import com.example.X_O.web.model.RefreshJwtRequest;
import com.example.X_O.web.model.SignUpRequest;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

	private final UserService userService;
	private final JwtProvider jwtProvider;

	public AuthService(UserService userService, JwtProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	public boolean register(SignUpRequest request) {
		if (request == null || request.getLogin() == null || request.getPassword() == null) {
			return false;
		}
		return userService.register(request.getLogin().trim(), request.getPassword());
	}

	public JwtResponse login(JwtRequest request) {
		if (request == null || request.getLogin() == null || request.getPassword() == null) {
			return null;
		}
		return userService.authenticate(request.getLogin().trim(), request.getPassword())
				.map(this::createTokenPair)
				.orElse(null);
	}

	public JwtResponse refreshAccessToken(RefreshJwtRequest request) {
		User user = findByRefreshToken(request);
		if (user == null) {
			return null;
		}
		return new JwtResponse(
				"Bearer",
				jwtProvider.generateAccessToken(user),
				request.getRefreshToken());
	}

	public JwtResponse refreshRefreshToken(RefreshJwtRequest request) {
		User user = findByRefreshToken(request);
		return user == null ? null : createTokenPair(user);
	}

	public JwtAuthentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthentication jwtAuthentication && jwtAuthentication.isAuthenticated()) {
			return jwtAuthentication;
		}
		return null;
	}

	private JwtResponse createTokenPair(User user) {
		return new JwtResponse(
				"Bearer",
				jwtProvider.generateAccessToken(user),
				jwtProvider.generateRefreshToken(user));
	}

	private User findByRefreshToken(RefreshJwtRequest request) {
		if (request == null || !jwtProvider.validateRefreshToken(request.getRefreshToken())) {
			return null;
		}
		try {
			Claims claims = jwtProvider.getRefreshClaims(request.getRefreshToken());
			UUID userId = UUID.fromString(claims.get("uuid", String.class));
			Optional<User> user = userService.findById(userId);
			return user.orElse(null);
		} catch (RuntimeException ex) {
			return null;
		}
	}
}
