package com.example.X_O.domain.model;

import java.util.UUID;

public final class LeaderboardEntry {

	private final UUID userId;
	private final String login;
	private final double winRatio;

	public LeaderboardEntry(UUID userId, String login, double winRatio) {
		this.userId = userId;
		this.login = login;
		this.winRatio = winRatio;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getLogin() {
		return login;
	}

	public double getWinRatio() {
		return winRatio;
	}
}
