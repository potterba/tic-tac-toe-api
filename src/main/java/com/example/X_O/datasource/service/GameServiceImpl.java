package com.example.X_O.datasource.service;

import com.example.X_O.datasource.mapper.GameMapper;
import com.example.X_O.datasource.repository.GameRepository;
import com.example.X_O.domain.model.Game;
import com.example.X_O.domain.model.GameState;
import com.example.X_O.domain.model.LeaderboardEntry;
import com.example.X_O.domain.service.GameService;
import com.example.X_O.domain.service.MinimaxGameService;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public final class GameServiceImpl implements GameService {

	private final MinimaxGameService minimaxDelegate = new MinimaxGameService();
	private final GameRepository gameRepository;

	public GameServiceImpl(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Override
	public Game getNextMove(Game game) {
		return minimaxDelegate.getNextMove(game);
	}

	@Override
	public boolean validateField(Game currentGame, int[][] newField, int symbol) {
		return minimaxDelegate.validateField(currentGame, newField, symbol);
	}

	@Override
	public boolean isGameOver(Game game) {
		return minimaxDelegate.isGameOver(game);
	}

	@Override
	public Game refreshState(Game game) {
		return minimaxDelegate.refreshState(game);
	}

	@Override
	public List<Game> getCompletedGames(UUID userId) {
		if (userId == null) {
			return List.of();
		}
		return gameRepository.findCompletedByUserId(userId, GameState.WIN, GameState.DRAW).stream()
				.map(GameMapper::toDomain)
				.toList();
	}

	@Override
	public List<LeaderboardEntry> getTopPlayers(int limit) {
		if (limit <= 0) {
			return List.of();
		}
		return gameRepository.findLeaderboard(PageRequest.of(0, limit)).stream()
				.map(row -> new LeaderboardEntry(
						row.getUserId(),
						row.getLogin(),
						row.getWinRatio() == null ? 0.0 : row.getWinRatio()))
				.toList();
	}
}
