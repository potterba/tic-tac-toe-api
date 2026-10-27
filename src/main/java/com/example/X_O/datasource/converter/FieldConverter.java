package com.example.X_O.datasource.converter;

import com.example.X_O.domain.model.GameField;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FieldConverter implements AttributeConverter<int[][], String> {

	@Override
	public String convertToDatabaseColumn(int[][] field) {
		if (field == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (int[] row : field) {
			for (int cell : row) {
				if (builder.length() > 0) {
					builder.append(',');
				}
				builder.append(cell);
			}
		}
		return builder.toString();
	}

	@Override
	public int[][] convertToEntityAttribute(String dbData) {
		int[][] field = new int[GameField.SIZE][GameField.SIZE];
		if (dbData == null || dbData.isBlank()) {
			return field;
		}
		String[] parts = dbData.split(",");
		int index = 0;
		for (int row = 0; row < GameField.SIZE; row++) {
			for (int col = 0; col < GameField.SIZE; col++) {
				if (index < parts.length && !parts[index].isBlank()) {
					field[row][col] = Integer.parseInt(parts[index].trim());
				}
				index++;
			}
		}
		return field;
	}
}
