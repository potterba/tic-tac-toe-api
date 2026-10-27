package com.example.X_O.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class Game {

	private final UUID id;
	private final GameField field;
	private final int playerSymbol;
	private final boolean vsComputer;
	private final GameState state;
	private final UUID xPlayerId;
	private final UUID oPlayerId;
	private final UUID turnPlayerId;
	private final UUID winnerId;
	private final Instant createdAt;

	public Game(UUID id, GameField field, int playerSymbol, boolean vsComputer, GameState state,
			UUID xPlayerId, UUID oPlayerId, UUID turnPlayerId, UUID winnerId, Instant createdAt) {
		this.id = id;
		this.field = field == null ? new GameField() : field;
		this.playerSymbol = playerSymbol == GameField.O ? GameField.O : GameField.X;
		this.vsComputer = vsComputer;
		this.state = state == null ? GameState.TURN : state;
		this.xPlayerId = xPlayerId;
		this.oPlayerId = oPlayerId;
		this.turnPlayerId = turnPlayerId;
		this.winnerId = winnerId;
		this.createdAt = createdAt == null ? Instant.now() : createdAt;
	}

	public UUID getId() {
		return id;
	}

	public GameField getField() {
		return field;
	}

	public int getPlayerSymbol() {
		return playerSymbol;
	}

	public boolean isVsComputer() {
		return vsComputer;
	}

	public GameState getState() {
		return state;
	}

	public UUID getXPlayerId() {
		return xPlayerId;
	}

	public UUID getOPlayerId() {
		return oPlayerId;
	}

	public UUID getTurnPlayerId() {
		return turnPlayerId;
	}

	public UUID getWinnerId() {
		return winnerId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Game withField(GameField newField) {
		return new Game(id, newField, playerSymbol, vsComputer, state, xPlayerId, oPlayerId, turnPlayerId, winnerId,
				createdAt);
	}

	public Game withPlayers(UUID newXPlayerId, UUID newOPlayerId) {
		return new Game(id, field, playerSymbol, vsComputer, state, newXPlayerId, newOPlayerId, turnPlayerId, winnerId,
				createdAt);
	}

	public Game withState(GameState newState, UUID newTurnPlayerId, UUID newWinnerId) {
		return new Game(id, field, playerSymbol, vsComputer, newState, xPlayerId, oPlayerId, newTurnPlayerId,
				newWinnerId, createdAt);
	}
}
