package com.example.X_O.service;

import com.example.X_O.domain.model.Role;
import com.example.X_O.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtProvider {

	private static final String UUID_CLAIM = "uuid";
	private static final String ROLES_CLAIM = "roles";
	private static final String TOKEN_TYPE_CLAIM = "type";
	private static final String ACCESS_TOKEN_TYPE = "access";
	private static final String REFRESH_TOKEN_TYPE = "refresh";

	private final SecretKey accessKey;
	private final SecretKey refreshKey;
	private final long accessExpirationMs;
	private final long refreshExpirationMs;

	public JwtProvider(
			@Value("${jwt.access.secret}") String accessSecret,
			@Value("${jwt.refresh.secret}") String refreshSecret,
			@Value("${jwt.access.expiration-ms}") long accessExpirationMs,
			@Value("${jwt.refresh.expiration-ms}") long refreshExpirationMs) {
		this.accessKey = createKey(accessSecret);
		this.refreshKey = createKey(refreshSecret);
		this.accessExpirationMs = requirePositive(accessExpirationMs, "accessExpirationMs");
		this.refreshExpirationMs = requirePositive(refreshExpirationMs, "refreshExpirationMs");
	}

	public String generateAccessToken(User user) {
		Objects.requireNonNull(user, "user");
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(user.getLogin())
				.claim(UUID_CLAIM, user.getId().toString())
				.claim(ROLES_CLAIM, user.getRoles().stream().map(Role::getAuthority).toList())
				.claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusMillis(accessExpirationMs)))
				.signWith(accessKey)
				.compact();
	}

	public String generateRefreshToken(User user) {
		Objects.requireNonNull(user, "user");
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(user.getId().toString())
				.claim(UUID_CLAIM, user.getId().toString())
				.claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusMillis(refreshExpirationMs)))
				.signWith(refreshKey)
				.compact();
	}

	public boolean validateAccessToken(String token) {
		return validateToken(token, accessKey, ACCESS_TOKEN_TYPE);
	}

	public boolean validateRefreshToken(String token) {
		return validateToken(token, refreshKey, REFRESH_TOKEN_TYPE);
	}

	public Claims getAccessClaims(String token) {
		return getClaims(token, accessKey);
	}

	public Claims getRefreshClaims(String token) {
		return getClaims(token, refreshKey);
	}

	private boolean validateToken(String token, SecretKey key, String expectedType) {
		if (token == null || token.isBlank()) {
			return false;
		}
		try {
			Claims claims = getClaims(token, key);
			return expectedType.equals(claims.get(TOKEN_TYPE_CLAIM, String.class));
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	private Claims getClaims(String token, SecretKey key) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private static SecretKey createKey(String secret) {
		if (secret == null || secret.isBlank()) {
			throw new IllegalArgumentException("JWT secret must not be blank");
		}
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	private static long requirePositive(long value, String name) {
		if (value <= 0) {
			throw new IllegalArgumentException(name + " must be positive");
		}
		return value;
	}
}
