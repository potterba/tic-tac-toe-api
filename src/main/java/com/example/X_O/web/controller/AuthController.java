package com.example.X_O.web.controller;

import com.example.X_O.service.AuthService;
import com.example.X_O.web.model.JwtRequest;
import com.example.X_O.web.model.JwtResponse;
import com.example.X_O.web.model.RefreshJwtRequest;
import com.example.X_O.web.model.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public boolean register(@RequestBody SignUpRequest request) {
		return authService.register(request);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		return response(authService.login(request));
	}

	@PostMapping("/token")
	public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshJwtRequest request) {
		return response(authService.refreshAccessToken(request));
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtResponse> refreshRefreshToken(@RequestBody RefreshJwtRequest request) {
		return response(authService.refreshRefreshToken(request));
	}

	private ResponseEntity<JwtResponse> response(JwtResponse response) {
		return response == null
				? ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
				: ResponseEntity.ok(response);
	}
}
