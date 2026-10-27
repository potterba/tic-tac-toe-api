package com.example.X_O.domain.model;

import java.util.Arrays;

public final class GameField {

	public static final int SIZE = 3;
	public static final int EMPTY = 0;
	public static final int X = 1;
	public static final int O = 2;

	private final int[][] cells;

	public GameField() {
		this.cells = new int[SIZE][SIZE];
	}

	public GameField(int[][] cells) {
		if (cells == null || cells.length != SIZE || cells[0].length != SIZE) {
			throw new IllegalArgumentException("Field must be " + SIZE + "x" + SIZE);
		}
		this.cells = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			this.cells[i] = Arrays.copyOf(cells[i], SIZE);
		}
	}

	public int get(int row, int col) {
		return cells[row][col];
	}

	public int[][] copyCells() {
		int[][] copy = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			copy[i] = Arrays.copyOf(cells[i], SIZE);
		}
		return copy;
	}

	public GameField withCell(int row, int col, int value) {
		int[][] next = copyCells();
		next[row][col] = value;
		return new GameField(next);
	}
}