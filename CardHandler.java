package fr.uge.patchwork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CardHandler {
	private final ArrayList<Card> _pile;
	private final ArrayList<Card> _discardPile;
	
	public CardHandler () {
		_pile = new ArrayList<Card>();
		_discardPile = new ArrayList<Card>();
	}
	
	public void initPile() {
		Collections.shuffle(_pile);
		addToDiscardPile(_pile.get(0));
		addToDiscardPile(_pile.get(1));
		_pile.remove(0);
		_pile.remove(1);
	}
	
	private void addToPile(Card card) {
		Objects.requireNonNull(card);
		_pile.add(card);
	}
	
	private void addToDiscardPile(Card card) {
		Objects.requireNonNull(card);
		_discardPile.add(card);
	}
	
	public void loadCards(Path path) throws IOException {
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			while ((line = reader.readLine()) != null) {
				var c = Card.parseLine(line);
				addToPile(c);
			}
		}
	}
	
	private void copyDiscardPileToPile() {
		for (var card : _discardPile) {
			_pile.add(card);
		}
	}
	
	public void restockEmptyPile() {
		Collections.shuffle(_discardPile);
		copyDiscardPileToPile();
		_discardPile.clear();
	}
	
	public Card drawCard() {
		if (_pile.size() == 0) {
			restockEmptyPile();
		}
		addToDiscardPile(_pile.get(0));
		_pile.remove(0);
		return _discardPile.get(_discardPile.size() - 1);
	}
	
	public void printAllCards() {
		for (var card : _pile) {
			System.out.println(card);
			for (var filter : card.filters()) {
				System.out.println(filter);
			}
			System.out.println();
		}
	}
}
