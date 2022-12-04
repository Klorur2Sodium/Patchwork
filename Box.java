import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class stores the information about a box, it's type (_status) 
 * and the players on it 
 * 
 * @author FRAIZE Victor
 * @author COUSSON Sophie
 *
 */

public class Box {
	private char _status;
	private ArrayList<Player> _players = new ArrayList<>();

	public Box(char status) {
		Objects.requireNonNull(status);

		switch (status) {
		case '|' -> _status = status;
		case '0' -> _status = status;
		case 'x' -> _status = status;
		default -> throw new IllegalArgumentException("status must be equal to |, 0 or x");
		}
	}
	
	/**
	 * The method returns the players List
	 * 
	 * @return a List of Players
	 */
	public List<Player> getPlayers() {
		return _players;
	}
	
	/**
	 * 
	 * The method returns a char that represent the type of the Box
	 * | or 0 or x (nothing, a button, a patch)
	 * 
	 * @return a char 
	 */
	public char getStatus() {
		return _status;	
	}

	/**
	 * The method adds a player to the List of Players
	 * 
	 * @param player to add
	 */
	public void add(Player player) {
		Objects.requireNonNull(player);
		_players.add(player);
	}

	/**
	 * The method removes a player form the List of players
	 * 
	 * @param player to remove
	 */
	public void remove(Player player) {
		Objects.requireNonNull(player);
		_players.remove(player);
	}

	/**
	 * The method returns the last player added to the List of players
	 * 
	 * @return a Player
	 */
	public Player getPlayer() {
		int playersSize = _players.size();

		if (playersSize > 0) {
			return _players.get(playersSize - 1);
		}
		return null;
	}

	/**
	 * The method launch events that match to the _status of the box.
	 * 
	 * @param player that plays
	 * @param scanner
	 * @param version the version of the game
	 */
	public void boxEvent(Player player, Scanner scanner, String version) {
		Objects.requireNonNull(player);

		switch (_status) {
		case '0':
			player.payEvent();
			break;
		case 'x':
			var patch = new Piece();
			patch.parseLine("1:0:0:0");
			player.placingPhase(patch, scanner, version);
			_status = '|';
			break;
		case '|':
			return;
		}
	}

	@Override
	public String toString() {
		return "" + _status;
	}
}
