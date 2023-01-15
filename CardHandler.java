package fr.uge.patchwork;

import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * This class stores the information about the Deck that the automa is using. It also handles 
 * all its action during the playing phase. Like shuffling the pile, drawing a card etc...
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class CardHandler extends GraphicalObject {
	private Constants _deck;
	private final ArrayList<Card> _pile;
	private final ArrayList<Card> _discard;
	
	/**
	 * Constructs a new CardHandler object.
	 */
	public CardHandler () {
		_pile = new ArrayList<Card>();
		_discard = new ArrayList<Card>();
		_deck = Constants.DEFAULT; 
	}
	
	/**
	 * Initializes the pile at the beginning of the game by shuffling it and adding two cards to the discard.
	 */
	public void initPile() {
		Collections.shuffle(_pile);
		addToDiscard(_pile.get(0));
		addToDiscard(_pile.get(1));
		_pile.remove(0);
		_pile.remove(1);
	}
	
	/**
	 * Adds a non null Card to the pile.
	 * @param card : the card you want to add.
	 */
	private void addToPile(Card card) {
		Objects.requireNonNull(card);
		_pile.add(card);
	}
	
	/**
	 * Adds a non null Card to the discard.
	 * @param card : the card you want to add.
	 */
	private void addToDiscard(Card card) {
		Objects.requireNonNull(card);
		_discard.add(card);
	}
	
	/**
	 * Loads the deck in the pile from a file.
	 * @param path : String representing the file's path.
	 * @param deck : enum indicating the deck used.
	 * @throws IOException when the path is invalid.
	 */
	public void loadCards(Path path, Constants deck) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(deck);
		_deck = deck;
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				var c = Card.parseLine(line);
				addToPile(c);
			}
		}
	}
	
	/**
	 * Copies the cards from the discard to the pile.
	 */
	private void copyDiscardPileToPile() {
		for (var card : _discard) {
			_pile.add(card);
		}
	}
	
	/**
	 * Shuffles the discards, adds the cards in the discard to the pile and clear the discard.
	 */
	public void restockEmptyPile() {
		Collections.shuffle(_discard);
		copyDiscardPileToPile();
		_discard.clear();
	}
	
	/**
	 * Simulates a card draw by removing a card from the pile to the discard. Also return that card.
	 * @return card drown.
	 */
	public Card drawCard() {
		if (_pile.size() == 0) {
			restockEmptyPile();
		}
		addToDiscard(_pile.get(0));
		_pile.remove(0);
		return _discard.get(_discard.size() - 1);
	}
	

	@Override
	protected void onDraw(Graphics2D graphics) {
		if (_pile.size() > 0 && _discard.size() > 0) {
			graphics.drawString("PILE", 55, 635);
			_pile.get(0).drawCardBack(graphics, 20, 650, 110, 180, _deck);
			graphics.drawString("DISCARD", 180, 635);
			_discard.get(_discard.size() - 1).drawCardFront(graphics, 150, 650, 110, 180);
		}
	}
}