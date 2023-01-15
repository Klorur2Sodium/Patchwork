package fr.uge.patchwork;

public enum SpecialeBox {
	WIN_LOSE, // one in two chance of stealing half of the opponent's buttons or giving away his half
	FREEZE, // the player can't go further 
	DOUBLE, // double your buttons
	SWITCH_POSITION, // the player in front takes the place of the one behind
	CHANCE, // the player can have the piece he wants for free
	SWITCH_BOARD, // the players exchange their boards 
	DRAW // the player can draw the piece he wants (4 bloc max)
}
