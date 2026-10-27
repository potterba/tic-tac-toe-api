package com.example.X_O.web.model;

import com.example.X_O.domain.model.GameState;

import java.time.Instant;
import java.util.UUID;

public final class GameDto {

	private UUID id;
	private int[][] field;
	private int playerSymbol = 1;
	private String opponent;
	private boolean vsComputer;
	private GameState state;
	private UUID xPlayerId;
	private UUID oPlayerId;
	private UUID turnPlayerId;
	private UUID winnerId;
	private Instant createdAt;

	public GameDto() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int[][] getField() {
		return field;
	}

	public void setField(int[][] field) {
		this.field = field;
	}

	public int getPlayerSymbol() {
		return playerSymbol;
	}

	public void setPlayerSymbol(int playerSymbol) {
		this.playerSymbol = playerSymbol;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public boolean isVsComputer() {
		return vsComputer;
	}

	public void setVsComputer(boolean vsComputer) {
		this.vsComputer = vsComputer;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public UUID getXPlayerId() {
		return xPlayerId;
	}

	public void setXPlayerId(UUID xPlayerId) {
		this.xPlayerId = xPlayerId;
	}

	public UUID getOPlayerId() {
		return oPlayerId;
	}

	public void setOPlayerId(UUID oPlayerId) {
		this.oPlayerId = oPlayerId;
	}

	public UUID getTurnPlayerId() {
		return turnPlayerId;
	}

	public void setTurnPlayerId(UUID turnPlayerId) {
		this.turnPlayerId = turnPlayerId;
	}

	public UUID getWinnerId() {
		return winnerId;
	}

	public void setWinnerId(UUID winnerId) {
		this.winnerId = winnerId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
