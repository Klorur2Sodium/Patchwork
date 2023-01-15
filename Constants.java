package fr.uge.patchwork;

/**
 * Nearly all the Constants needed for the game.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public enum Constants {
	/**
	 * Default enum
	 */
	DEFAULT(0),
	
	/**
	 * type of the boxes
	 */
 	EMPTY(1), PATCH(2), BUTTON(3),
 	
 	/**
 	 * type of a choice of piece in the Ascii versions
 	 */
 	SKIP(1), ONE(0), TWO(1), THREE(2),
 	
 	/**
 	 * indicates the chosen version of the game
 	 */
 	PHASE1(1), PHASE2(2), PHASE3(3), PHASE4(4), PHASE5(5),
 	
 	/**
 	 * indicates the difficulty
 	 */
 	INTERN(1), APPRENTICE(9), FELLOW(12), MASTER(15), LEGEND(18),
 	
 	/**
 	 * type of filter
 	 */
 	LESS_MOVE(0), MOST_BUTTON(1), BIGGEST_PIECE(2), FARTHEST_PIECE(3),
 	
 	/**
 	 * type of deck
 	 */
 	NORMAL_DECK(1), TACTICAL_DECK(2),
 	
 	/**
 	 * Color of a pawn
 	 */
 	BLUE(1), RED(2), DARK_GREY(3), GREEN(4),
 	
 	/**
 	 * type of comment
 	 */
 	SMALL_COMMENT(5), BIG_COMMENT(6),
 	
 	/**
 	 * quiltboard size
 	 */
 	GRID_SIZE(9),
 	
 	/**
 	 * graphic enum
 	 */
 	WINDOW_SIZE(25), BOX_SIZE(75), PIECE_SQUARE(20),
 	
 	/**
 	 * graphic enum
 	 */
	GRAPHIC_HEIGHT(1), GRAPHIC_WIDTH(2),
	
	/**
	 * special tile
	 */
	SPECIAL_TILE(7);
	
	private final int _value;
	
	/**
	 * Initiates a new Constants witch a value.
	 * @param value
	 */
	private Constants(int value) {
		_value = value;
	}
	
	/**
	 * Getter for the value of a Constants.
	 * @return value of a Constants.
	 */
	public int getValue() {
	    return _value;
	}
}