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
 	PHASE1(1), PHASE2(2), PHASE3(3), PHASE4(4), PHASE5(5),
 	INTERN(1), APPRENTICE(9), FELLOW(12), MASTER(15), LEGEND(18),
 	LESS_MOVE(0), MOST_BUTTON(1), BIGGEST_PIECE(2), FARTHEST_PIECE(3),
 	NORMAL_DECK(1), TACTICAL_DECK(2),
 	BLUE(1), RED(2), DARK_GREY(3), GREEN(4),
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