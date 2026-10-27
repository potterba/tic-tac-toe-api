package com.example.X_O.datasource.model;

import com.example.X_O.datasource.converter.FieldConverter;
import com.example.X_O.domain.model.GameState;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "games")
public class GameEntity {

	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Convert(converter = FieldConverter.class)
	@Column(nullable = false, length = 32)
	private int[][] field;

	@Column(name = "player_symbol", nullable = false)
	private int playerSymbol;

	@Column(name = "vs_computer")
	private Boolean vsComputer;

	@Enumerated(EnumType.STRING)
	@Column(length = 16)
	private GameState state;

	@Column(name = "x_player_id")
	private UUID xPlayerId;

	@Column(name = "o_player_id")
	private UUID oPlayerId;

	@Column(name = "turn_player_id")
	private UUID turnPlayerId;

	@Column(name = "winner_id")
	private UUID winnerId;

	@Column(
			name = "created_at",
			nullable = false,
			updatable = false,
			columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private Instant createdAt;

	protected GameEntity() {
	}

	public GameEntity(UUID id, int[][] field, int playerSymbol, Boolean vsComputer, GameState state,
			UUID xPlayerId, UUID oPlayerId, UUID turnPlayerId, UUID winnerId, Instant createdAt) {
		this.id = id;
		this.field = field == null ? new int[3][3] : copy(field);
		this.playerSymbol = playerSymbol;
		this.vsComputer = vsComputer;
		this.state = state;
		this.xPlayerId = xPlayerId;
		this.oPlayerId = oPlayerId;
		this.turnPlayerId = turnPlayerId;
		this.winnerId = winnerId;
		this.createdAt = createdAt == null ? Instant.now() : createdAt;
	}

	public UUID getId() {
		return id;
	}

	public int[][] getField() {
		return copy(field);
	}

	public int getPlayerSymbol() {
		return playerSymbol;
	}

	public Boolean getVsComputer() {
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

	private static int[][] copy(int[][] a) {
		int[][] c = new int[a.length][];
		for (int i = 0; i < a.length; i++) {
			c[i] = Arrays.copyOf(a[i], a[i].length);
		}
		return c;
	}
}
