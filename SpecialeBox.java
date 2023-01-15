package fr.uge.patchwork;

/**
 * Enum that represents the different special tiles.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public enum SpecialeBox {
	/**
	 * one in two chance of stealing half of the opponent's buttons or giving away his half
	 */
	WIN_LOSE,
	/**
	 * the player can't go further 
	 */
	FREEZE, 
	/**
	 * double your buttons
	 */
	DOUBLE, 
	/**
	 * the player in front takes the place of the one behind
	 */
	SWITCH_POSITION, 
	/**
	 * the player can have the piece he wants for free
	 */
	CHANCE, // 
	/**
	 * the players exchange their boards 
	 */
	SWITCH_BOARD, 
	/**
	 * the player can draw the piece he wants (4 bloc max)
	 */
	DRAW 
}
