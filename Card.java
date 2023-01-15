package fr.uge.patchwork;

import java.util.ArrayList;
import java.util.Objects;

public record Card (int turnBudget, int wage, Filter[] filters) {
	public Card {
		Objects.requireNonNull(filters);
		if (turnBudget < 0 || wage < 0) {
			throw new IllegalArgumentException("turnBudget and wage must be positive.");
		}
	}
	
	public static Card parseLine(String line) {
		Objects.requireNonNull(line);
		var filters = new Filter[3];
		var splitLine = line.split(":");
		for (int i = 2; i < splitLine.length; i++) {
			filters[i - 2] = new Filter(Integer.parseInt(splitLine[i]));
		}
		return new Card(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]), filters);
	}
	
	public Piece applyFilters(ArrayList<Piece> pieces, int AutomaPos, int playerPos) {
		var newPieces = pieces;
		
		for (int i = 0; i < 3 && newPieces.size() > 1; i++) {
			if (filters[i] != null) {
				newPieces = filters[i].applyFilter(pieces, AutomaPos, playerPos);
			}
		}
		return newPieces.get(0);
	}
}