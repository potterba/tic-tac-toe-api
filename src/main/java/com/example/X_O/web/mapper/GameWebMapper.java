package com.example.X_O.web.mapper;

import com.example.X_O.domain.model.Game;
import com.example.X_O.web.model.GameDto;

import java.util.Arrays;

public final class GameWebMapper {

	private GameWebMapper() {
	}

	public static GameDto toDto(Game game) {
		if (game == null) {
			return null;
		}
		GameDto dto = new GameDto();
		dto.setId(game.getId());
		dto.setField(copy(game.getField().copyCells()));
		dto.setPlayerSymbol(game.getPlayerSymbol());
		dto.setVsComputer(game.isVsComputer());
		dto.setOpponent(game.isVsComputer() ? "COMPUTER" : "USER");
		dto.setState(game.getState());
		dto.setXPlayerId(game.getXPlayerId());
		dto.setOPlayerId(game.getOPlayerId());
		dto.setTurnPlayerId(game.getTurnPlayerId());
		dto.setWinnerId(game.getWinnerId());
		dto.setCreatedAt(game.getCreatedAt());
		return dto;
	}

	private static int[][] copy(int[][] source) {
		if (source == null) {
			return null;
		}
		int[][] copy = new int[source.length][];
		for (int i = 0; i < source.length; i++) {
			copy[i] = Arrays.copyOf(source[i], source[i].length);
		}
		return copy;
	}
}
