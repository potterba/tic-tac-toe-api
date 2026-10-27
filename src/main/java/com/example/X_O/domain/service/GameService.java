package com.example.X_O.domain.service;

import com.example.X_O.domain.model.Game;
import com.example.X_O.domain.model.LeaderboardEntry;

import java.util.List;
import java.util.UUID;

public interface GameService {

	Game getNextMove(Game game);

	boolean validateField(Game currentGame, int[][] newField, int symbol);

	boolean isGameOver(Game game);

	Game refreshState(Game game);

	List<Game> getCompletedGames(UUID userId);

	List<LeaderboardEntry> getTopPlayers(int limit);
}