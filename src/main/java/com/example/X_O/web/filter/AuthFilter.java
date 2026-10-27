package com.example.X_O.web.filter;

import com.example.X_O.domain.model.JwtAuthentication;
import com.example.X_O.service.JwtProvider;
import com.example.X_O.service.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Set;

public class AuthFilter extends GenericFilterBean {

	private static final Set<String> PUBLIC_PATHS = Set.of(
			"/auth/register",
			"/auth/login",
			"/auth/token",
			"/auth/refresh",
			"/",
			"/index.html",
			"/favicon.ico",
			"/error"
	);

	private final JwtProvider jwtProvider;
	private final JwtUtil jwtUtil;

	public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
		this.jwtProvider = jwtProvider;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (isPublic(httpRequest)) {
			chain.doFilter(request, response);
			return;
		}
		String token = getBearerToken(httpRequest.getHeader("Authorization"));
		if (!jwtProvider.validateAccessToken(token)) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setHeader("WWW-Authenticate", "Bearer");
			return;
		}
		try {
			Claims claims = jwtProvider.getAccessClaims(token);
			JwtAuthentication authentication = jwtUtil.createAuthentication(claims);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (RuntimeException ex) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setHeader("WWW-Authenticate", "Bearer");
			return;
		}
		try {
			chain.doFilter(request, response);
		} finally {
			SecurityContextHolder.clearContext();
		}
	}

	private static boolean isPublic(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		return PUBLIC_PATHS.contains(path);
	}

	private static String getBearerToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}
		String token = authorizationHeader.substring(7).trim();
		return token.isEmpty() ? null : token;
	}
}
