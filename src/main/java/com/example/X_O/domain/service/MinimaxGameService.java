package com.example.X_O.domain.service;

import com.example.X_O.domain.model.Game;
import com.example.X_O.domain.model.GameField;
import com.example.X_O.domain.model.GameState;

import java.util.Arrays;
import java.util.UUID;

public final class MinimaxGameService {

	public Game getNextMove(Game game) {
		int playerSym = game.getPlayerSymbol();
		int computerSym = (playerSym == GameField.X) ? GameField.O : GameField.X;
		GameField field = game.getField();
		int[][] cells = field.copyCells();
		int[] best = minimax(cells, true, computerSym, playerSym);
		if (best[0] >= -1 && best[1] >= 0 && best[2] >= 0) {
			cells[best[1]][best[2]] = computerSym;
			return game.withField(new GameField(cells));
		}
		return game;
	}

	private int[] minimax(int[][] board, boolean isMax, int computerSym, int playerSym) {
		Integer winner = getWinner(board);
		if (winner != null) {
			if (winner == computerSym) return new int[] { 1, -1, -1 };
			if (winner == playerSym) return new int[] { -1, -1, -1 };
		}
		if (isFull(board)) return new int[] { 0, -1, -1 };

		int bestScore = isMax ? -2 : 2;
		int bestRow = -1, bestCol = -1;
		int playSym = isMax ? computerSym : playerSym;

		for (int r = 0; r < GameField.SIZE; r++) {
			for (int c = 0; c < GameField.SIZE; c++) {
				if (board[r][c] != GameField.EMPTY) continue;
				board[r][c] = playSym;
				int[] next = minimax(board, !isMax, computerSym, playerSym);
				board[r][c] = GameField.EMPTY;
				int score = next[0];
				if (isMax && score > bestScore) {
					bestScore = score;
					bestRow = r;
					bestCol = c;
				} else if (!isMax && score < bestScore) {
					bestScore = score;
					bestRow = r;
					bestCol = c;
				}
			}
		}
		return new int[] { bestScore, bestRow, bestCol };
	}

	public boolean validateField(Game currentGame, int[][] newField, int symbol) {
		if (newField == null || newField.length != GameField.SIZE) return false;
		int playerSym = symbol;
		int[][] current = currentGame.getField().copyCells();
		int diffCount = 0;
		int addedRow = -1, addedCol = -1;
		for (int i = 0; i < GameField.SIZE; i++) {
			if (newField[i] == null || newField[i].length != GameField.SIZE) return false;
			for (int j = 0; j < GameField.SIZE; j++) {
				if (newField[i][j] != current[i][j]) {
					if (current[i][j] != GameField.EMPTY) return false;
					if (newField[i][j] != playerSym) return false;
					diffCount++;
					addedRow = i;
					addedCol = j;
				}
			}
		}
		return diffCount == 1 && addedRow >= 0;
	}

	public boolean isGameOver(Game game) {
		return game.getState() == GameState.WIN || game.getState() == GameState.DRAW;
	}

	public Game refreshState(Game game) {
		if (game.getState() == GameState.WAITING) {
			return game;
		}
		int[][] cells = game.getField().copyCells();
		Integer winner = getWinner(cells);
		if (winner != null) {
			UUID winnerId = winner == GameField.X ? game.getXPlayerId() : game.getOPlayerId();
			return game.withState(GameState.WIN, null, winnerId);
		}
		if (isFull(cells)) {
			return game.withState(GameState.DRAW, null, null);
		}
		int crosses = 0;
		int noughts = 0;
		for (int[] row : cells) {
			for (int cell : row) {
				if (cell == GameField.X) {
					crosses++;
				} else if (cell == GameField.O) {
					noughts++;
				}
			}
		}
		UUID turnPlayerId = crosses == noughts ? game.getXPlayerId() : game.getOPlayerId();
		return game.withState(GameState.TURN, turnPlayerId, null);
	}

	private static Integer getWinner(int[][] board) {
		for (int i = 0; i < GameField.SIZE; i++) {
			if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2])
				return board[i][0];
			if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i])
				return board[0][i];
		}
		if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2])
			return board[0][0];
		if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
			return board[0][2];
		return null;
	}

	private static boolean isFull(int[][] board) {
		for (int[] row : board)
			for (int c : row)
				if (c == GameField.EMPTY) return false;
		return true;
	}
}