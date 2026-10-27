package com.example.X_O.di;

import com.example.X_O.datasource.repository.GameRepository;
import com.example.X_O.datasource.repository.UserRepository;
import com.example.X_O.datasource.service.GameServiceImpl;
import com.example.X_O.datasource.service.UserServiceImpl;
import com.example.X_O.domain.service.GameService;
import com.example.X_O.domain.service.UserService;
import com.example.X_O.service.AuthService;
import com.example.X_O.service.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

	@Bean
	public GameService gameService(GameRepository gameRepository) {
		return new GameServiceImpl(gameRepository);
	}

	@Bean
	public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new UserServiceImpl(userRepository, passwordEncoder);
	}

	@Bean
	public AuthService authService(UserService userService, JwtProvider jwtProvider) {
		return new AuthService(userService, jwtProvider);
	}
}
