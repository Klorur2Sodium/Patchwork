package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class stores the information about a Card. It also handles the application of its
 * filters on an ArrayList of pieces.
 * 
 * @param turnBudget of the Card.
 * @param wage of the Card.
 * @param filters of the Card.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public record Card(int turnBudget, int wage, Filter[] filters) {
	/**
	 * Construct a new Card.
	 * @param turnBudget of the Card.
	 * @param wage of the Card.
	 * @param filters of the Card.
	 */
	public Card {
		Objects.requireNonNull(filters);
		if (turnBudget < 0 || wage < 0) {
			throw new IllegalArgumentException("turnBudget and wage must be positive.");
		}
	}
	
	/**
	 * Returns a new Card by parsing the given String.
	 * @param line : contains the data of a Card.
	 * @return a new Card object.
	 */
	public static Card parseLine(String line) {
		Objects.requireNonNull(line);
		var filters = new Filter[3];
		var splitLine = line.split(":");
		for (int i = 2; i < splitLine.length; i++) {
			filters[i - 2] = new Filter(Integer.parseInt(splitLine[i]));
		}
		return new Card(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]), filters);
	}
	
	/**
 * Applies all the filters of a function on an ArrayList of pieces and returns the piece that satisfies the filters.
	 * @param pieces : ArrayList to filter.
	 * @param AutomaPos : postion of the automa.
	 * @param playerPos : position of the player.
	 * @return the piece that satisfies the filters.
	 */
	public Piece applyFilters(ArrayList<Piece> pieces, int AutomaPos, int playerPos) {
		Objects.requireNonNull(pieces);
		if (AutomaPos < 0 || playerPos < 0) {
			throw new IllegalArgumentException("Invalid position");
		}
		var newPieces = pieces;
		
		for (int i = 0; i < 3 && newPieces.size() > 1; i++) {
			if (filters[i] != null) {
				newPieces = filters[i].applyFilter(pieces, AutomaPos, playerPos);
			}
		}
		return newPieces.get(0);
	}
	
	/**
	 * Draws the front of a card.
	 * @param  graphics : object that calls the graphic methods.
	 * @param topLeftX : x coordinates of the top left corner.
	 * @param topLeftY : y coordinates of the top left corner.
	 * @param width : width of the card.
	 * @param height : height of the card.
	 */
	public void drawCardFront(Graphics2D graphics, int topLeftX, int topLeftY, int width, int height) {
		Objects.requireNonNull(graphics);
		var rect = new Rectangle2D.Float(topLeftX, topLeftY, width, height);
		var yPadding = 35;
		var y = topLeftY + 45;
		graphics.draw(rect);
		graphics.drawLine(topLeftX, topLeftY + 30, topLeftX + width, topLeftY + 30);
		graphics.drawString("" + turnBudget, (topLeftX + width) - (width / 2), topLeftY + 20);
		for (int i = 0; i < 3; i++) {
			if (filters[i] != null) {
				graphics.drawString(i + ".", (topLeftX + width) - (width / 2), y);
				filters[i].drawFilter(graphics, topLeftX + 5, y + 13);
				y += yPadding;
			}
		}
		graphics.drawString("+" + wage, topLeftX + width - 25, topLeftY + height - 15);
	}
	
	/**
	 * Draws the back of a card.
	 * @param graphics : object that calls the graphic methods.
	 * @param topLeftX : x coordinates of the top left corner.
	 * @param topLeftY : y coordinates of the top left corner.
	 * @param width : width of the card.
	 * @param height : height of the card.
	 * @param deck : indicates the deck used.
	 */
	public void drawCardBack(Graphics2D graphics, int topLeftX, int topLeftY, int width, int height, Constants deck) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(deck);
		var rect = new Rectangle2D.Float(topLeftX, topLeftY, width, height);
		graphics.draw(rect);
		graphics.drawLine(topLeftX, topLeftY + 30, topLeftX + width, topLeftY + 30);
		if (deck == Constants.TACTICAL_DECK) {
			graphics.drawString("" + turnBudget, (topLeftX + width) - (width / 2), topLeftY + 20);
		}
	}
}