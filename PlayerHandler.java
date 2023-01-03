package fr.uge.patchwork;

import java.util.Objects;

import fr.umlv.zen5.ApplicationContext;

/**
 * The class stores an array of players and handles the current player, 
 * the end of the game and the winner.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class PlayerHandler {
	private final Player[] _players;
	private int _current;
	private boolean _specialTilesRemaining;
	
	/**
	 * Constructs a new PlayerHandler.
	 * 
	 * @param players : the players
	 * @param special : the special tile
	 */
	public PlayerHandler(Player[] players, boolean special) {
		Objects.requireNonNull(players);
		_players = players;
		_specialTilesRemaining = special;
		_current = 0;
	}
	
	/**
	 * Returns the player located at the given index.
	 * 
	 * @param i : index
	 * @return player
	 */
	public Player getPlayerIndex(int i) {
		if (i < 0 || i > 1) {
			throw new IllegalArgumentException("there is only two players");
		}
		return _players[i];
	}
	
	/**
	 * Returns the current player.
	 * 
	 * @return current player
	 */
	public Player getCurrent() {
		return _players[_current];
	}
	
	/**
	 * Returns true if there is a special tile left.
	 * 
	 * @return boolean
	 */
	public boolean specialTileRemaining() {
		return _specialTilesRemaining;
	}
	
	/**
	 * Changes the state of _specialTile if the special tile is given to
	 * a player
	 */
	public void updateSpecialTile() {
		if (specialTileRemaining()) {
			if (_players[_current].getQuiltboard().checkSpecialTile()) {
				_players[_current].addSpecialTile();
				_specialTilesRemaining = false;
				System.out.println("okok");
			}
		}
	}
	
	/**
	 * Updates the current player.
	 */
	public void updateCurrentPlayer() {
        if (_players[0].getPosition() > _players[1].getPosition()) {
            _current =  1;
        } else if (_players[0].getPosition() < _players[1].getPosition()) {
            _current =  0;
        }
    }
	
	/**
	 * Returns the distance between the players.
	 * 
	 * @return distance
	 */
	public int distanceBetweenPlayers() {
		return Math.abs(_players[0].getPosition() - _players[1].getPosition());
	}
	
	/**
	 * Checks whether or not the game is finished
	 * 
	 * @param boardSize : size of the board
	 * @return boolean
	 */
	public boolean checkEndOfGame(int boardSize) {
		return _players[0].getPosition() == _players[1].getPosition() && _players[0].getPosition() == (boardSize - 1);
	}
	
	/**
	 * Returns the player with the greatest score.
	 * 
	 * @return best player
	 */
	private Player getVictoriousPlayer() {
		return (_players[0].getScore() > _players[1].getScore()) ? _players[0] : _players[1];
	}
	
	/**
	 * Displays the scores of the best player.
	 */
	public void displayWinner() {
		var winner = getVictoriousPlayer();
		System.out.println(winner.getName() + " won with " + winner.getScore() + " points");
	}
	
	/**
	 * The function calls the drawing function of the current player
	 * @param context
	 * @param topX
	 * @param topY
	 */
	public void draw(ApplicationContext context, float topX, float topY) {
		_players[_current].draw(context, topX, topY);
	}
	
}










