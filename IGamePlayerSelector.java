package fr.uge.patchwork;

/**
 * This interface expose a method 
 * to set the players.
 * Once the players are set the next step is to build a game
 * Thus the addPlayer method returns the IGameBuilder Interface.
 * 
 * @author Cousson Sophie
 * @author Fraize Victor
 */
public interface IGamePlayerSelector {
	/**
	 * Creates the players based on their names.
	 * 
	 * @return IGameBuilder
	 */
	IGameBuilder addPlayers();
}
