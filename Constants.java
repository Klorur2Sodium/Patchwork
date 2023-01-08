package fr.uge.patchwork;

/**
 * 
 * @author FRAIZE Victor
 * @author COUSSON Sophie
 *
 */
public enum Constants {
	DEFAULT(0),
	EMPTY(1), PATCH(2), BUTTON(3),
	SKIP(1), ONE(0), TWO(1), THREE(2),
	PHASE1(1), PHASE2(2), PHASE3(3),
	BLUE(1), RED(2), GREEN(3),
	SMALL_COMMENT(5), BIG_COMMENT(6),
	GRID_SIZE(9),
	WINDOW_SIZE(25), BOX_SIZE(75), PIECE_SQUARE(20),
	GRAPHIC_HEIGHT(1), GRAPHIC_WIDTH(2),
	SPECIAL_TILE(7);
	
	private final int _value;
	
	private Constants(int value) {
	    _value = value;
	  }

	public int getValue() {
	    return _value;
	}
}