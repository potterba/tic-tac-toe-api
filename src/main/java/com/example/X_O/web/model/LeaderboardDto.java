package com.example.X_O.web.model;

import java.util.UUID;

public class LeaderboardDto {

	private UUID userId;
	private String login;
	private double winRatio;

	public LeaderboardDto() {
	}

	public LeaderboardDto(UUID userId, String login, double winRatio) {
		this.userId = userId;
		this.login = login;
		this.winRatio = winRatio;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public double getWinRatio() {
		return winRatio;
	}

	public void setWinRatio(double winRatio) {
		this.winRatio = winRatio;
	}
}
