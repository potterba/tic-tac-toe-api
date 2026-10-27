package com.example.X_O.datasource.mapper;

import com.example.X_O.datasource.model.GameEntity;
import com.example.X_O.domain.model.Game;
import com.example.X_O.domain.model.GameField;
import com.example.X_O.domain.model.GameState;

public final class GameMapper {

	private GameMapper() {
	}

	public static GameEntity toEntity(Game game) {
		if (game == null) {
			return null;
		}
		return new GameEntity(
				game.getId(),
				game.getField().copyCells(),
				game.getPlayerSymbol(),
				game.isVsComputer(),
				game.getState(),
				game.getXPlayerId(),
				game.getOPlayerId(),
				game.getTurnPlayerId(),
				game.getWinnerId(),
				game.getCreatedAt());
	}

	public static Game toDomain(GameEntity entity) {
		if (entity == null) {
			return null;
		}
		boolean vsComputer = entity.getVsComputer() == null || entity.getVsComputer();
		GameState state = entity.getState() == null ? GameState.TURN : entity.getState();
		return new Game(
				entity.getId(),
				new GameField(entity.getField()),
				entity.getPlayerSymbol(),
				vsComputer,
				state,
				entity.getXPlayerId(),
				entity.getOPlayerId(),
				entity.getTurnPlayerId(),
				entity.getWinnerId(),
				entity.getCreatedAt());
	}
}
