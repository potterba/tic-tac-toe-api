package com.example.X_O.web.controller;

import com.example.X_O.domain.model.JwtAuthentication;
import com.example.X_O.domain.model.User;
import com.example.X_O.domain.service.UserService;
import com.example.X_O.service.AuthService;
import com.example.X_O.web.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final AuthService authService;

	public UserController(UserService userService, AuthService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUser() {
		JwtAuthentication authentication = authService.getAuthentication();
		if (authentication == null) {
			return ResponseEntity.status(401).build();
		}
		return getUser(authentication.getPrincipal());
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<UserDto> getUser(@PathVariable UUID uuid) {
		return userService.findById(uuid)
				.map(this::toDto)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	private UserDto toDto(User user) {
		return new UserDto(user.getId(), user.getLogin(), user.getRoles());
	}
}
