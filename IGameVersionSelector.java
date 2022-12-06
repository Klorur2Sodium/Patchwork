package fr.uge.patchwork;

/**
 * This interface expose a method 
 * to choose a game version.
 * Once the version is set. The next step is to select the players
 * Thus the chooseVersion returns the interface exposing the player selection/setting
 * 
 * @author Cousson Sophie
 * @author Fraize Victor
 */
public interface IGameVersionSelector {
	/**
	 * Chooses the version that the players selected
	 * 
	 * @return IGamePlayerSelector
	 */
	IGamePlayerSelector chooseVersion();
}
