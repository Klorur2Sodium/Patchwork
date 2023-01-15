package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class stores the information about a filter of a card like its filterId.
 * Can be used to apply a filter to a list of Piece.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public record Filter(Constants filterId) {
	/**
	 * Construct a new filter object with the given filterId.
	 * @param filterId : indicates the filterId of this Filter.
	 */
	public Filter {
		if (filterId == Constants.DEFAULT) {
			throw new IllegalArgumentException("filterId is invalid.");
		}
	}
	
	/**
	 * Construct a new filter object with the given filterId in int.
	 * @param filterIdInt : int indicating the filterId of this Filter.
	 */
	public Filter(int filterIdInt) {
		this(readFilter(filterIdInt));
	}
	
	/**
	 * Parses an int into the corresponding Constants enum
	 * @param filterIdInt
	 * @return
	 */
	private static Constants readFilter(int filterIdInt) {
		return switch(filterIdInt) {
			case(0) -> Constants.LESS_MOVE;
			case(1) -> Constants.MOST_BUTTON;
			case(2) -> Constants.BIGGEST_PIECE;
			case(3) -> Constants.FARTHEST_PIECE;
			default -> Constants.DEFAULT;
		};
	}
	
	/**
	 * Apply the filter of this Filter object on the given list pieces using its filterId.
	 * @param pieces : The list that contains the pieces to filter.
 	 * @param automaPos : Position of the automa.
	 * @param playerPos : Position of the player.
	 * @return the filtered list.
	 */
	public ArrayList<Piece> applyFilter(ArrayList<Piece> pieces, int automaPos, int playerPos) {
		Objects.requireNonNull(pieces);
		if (automaPos < 0 || playerPos < 0) {
			throw new IllegalArgumentException("Invalid position");
		}
		var filteredList = new ArrayList<Piece>();
		Objects.requireNonNull(pieces);
		if (pieces.size() < 2) {
			throw new IllegalArgumentException("List pieces must at least contain 2 element");
		}
		
		switch (filterId) {
		case LESS_MOVE -> filteredList = keepTurnFilter(pieces, automaPos, playerPos);
		case MOST_BUTTON -> filteredList = mostButtonFilter(pieces);
		case BIGGEST_PIECE -> filteredList = largestPieceFilter(pieces);
		case FARTHEST_PIECE -> filteredList = furthestPatchFilter(pieces);
		default -> throw new IllegalArgumentException("Unexpected value: " + filterId);
		}
		
		return filteredList;
	}
	
	/**
	 * Filters the list pieces so there are, at the end, only pieces that moves the automa before
	 * or on the player. That way, the automa can play again. 
	 * @param pieces : The list that contains the pieces to filter.
 	 * @param automaPos : Position of the automa.
	 * @param playerPos : Position of the player.
	 * @return the filtered list.
	 */
	private ArrayList<Piece> keepTurnFilter(ArrayList<Piece> pieces, int automaPos, int playerPos) {
		 var newPieces = new ArrayList<Piece>();
		 Piece piece;
		 for (int i = 0; i < pieces.size(); i++) {
			 piece = pieces.get(i);
			 if (piece.getMoves() + automaPos <= playerPos) {
				 newPieces.add(piece);
			 }
		 }
		 return (newPieces.size() > 0) ? newPieces : pieces;
	}
	
	/**
	 * Filters the list pieces so that this function returns a list that contains the pieces
	 * with the most buttons. 
	 * @param pieces : The list that contains the pieces to filter.
	 * @return the filtered list.
	 */
	private ArrayList<Piece> mostButtonFilter(List<Piece> pieces) {
		var newPieces = new ArrayList<Piece>();
		Piece piece;
		newPieces.add(pieces.get(0));
		
		for (int i = 1; i < pieces.size(); i++) {
		  piece = pieces.get(i);
			if (piece.getButtons() > newPieces.get(0).getButtons()) {
				newPieces = new ArrayList<Piece>();
				newPieces.add(piece);
			}
			else if (piece.getButtons() == newPieces.get(0).getButtons()) {
				newPieces.add(piece);
			}
		}

		return newPieces;
	}
	
	/**
	 * Filters the list pieces so that this function returns a list that contains the pieces
	 * with the largest buttons. 
	 * @param pieces : The list that contains the pieces to filter.
	 * @return the filtered list.
	 */
	private ArrayList<Piece> largestPieceFilter(List<Piece> pieces) {
		var newPieces = new ArrayList<Piece>();
		Piece piece;
		newPieces.add(pieces.get(0));
		
		for (int i = 1; i < pieces.size(); i++) {
		  piece = pieces.get(i);
			if (piece.getNumberOfBodyParts() > newPieces.get(0).getNumberOfBodyParts()) {
				newPieces.clear();
				newPieces.add(piece);
			}
			else if (piece.getNumberOfBodyParts() == newPieces.get(0).getNumberOfBodyParts()) {
				newPieces.add(piece);
			}
		}
		return newPieces;
	}
	
	/**
	 * Returns the last element of pieces in a list.
	 * @param pieces : The list that contains the pieces to filter.
	 * @return list containing the last element of the list pieces.
	 */
	private ArrayList<Piece> furthestPatchFilter(List<Piece> pieces) {
		var newPieces = new ArrayList<Piece>();
		newPieces.add(pieces.get(pieces.size() - 1));
		return newPieces;
	}

	public void drawFilter(Graphics2D graphics, int x, int y) {
		Objects.requireNonNull(graphics);
		switch(filterId) {
		case LESS_MOVE -> graphics.drawString("Keeps its turn", x, y);
		case MOST_BUTTON -> {
			graphics.drawString("Piece with the", x, y);
			graphics.drawString("most buttons", x, y + 9);
		}
		case BIGGEST_PIECE -> graphics.drawString("Biggest piece", x, y);
		case FARTHEST_PIECE -> {
			graphics.drawString("Farthest piece from", x, y);
			graphics.drawString("neutral token", x, y + 9);
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + filterId);
		}
		
	}
}