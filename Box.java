package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the information about a box, it's type (_status) 
 * and the players on it 
 * 
 * @author FRAIZE Victor
 * @author COUSSON Sophie
 */
public class Box {
	private Constants _status;
	private ArrayList<Player> _players = new ArrayList<>();

	/**
	* The method is the constructor it takes a char status and put it on the _status
	*
	* @param status : status of the box
	*/
	public Box(char status) {
		Objects.requireNonNull(status);

		switch (status) {
		case '|' -> _status = Constants.EMPTY;
		case '0' -> _status = Constants.BUTTON;
		case 'x' -> _status = Constants.PATCH;
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
	 * The method returns a char that represent the type of the Box
	 * | or 0 or x (nothing, a button, a patch)
	 * 
	 * @return a char 
	 */
	public Constants getStatus() {
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
	 * @param scanner for the x event
	 * @param version the version of the game
	 */
	public void boxEvent(Player player, Scanner scanner, Constants version) {
		Objects.requireNonNull(player);

		switch (_status) {
		case BUTTON:
			player.payEvent();
			break;
		case PATCH:
			var patch = new Piece();
			patch.parseLine("1:0:0:0");
			player.placingPhase(patch, scanner, version);
			_status = Constants.EMPTY;
			break;
		default : 
			return;
		}
	}
	
	/**
	 * The function draws the buttons or the patch that could be on a box on (x, y)
	 * @param context
	 * @param x
	 * @param y
	 */
	public void draw(ApplicationContext context, float x, float y) {
    	context.renderFrame(graphics -> {
    		switch(_status) {
			case BUTTON : 
				graphics.setColor(Color.CYAN);
				Ellipse2D.Float ellipse = new Ellipse2D.Float(x - 5, y - 5, 10, 10);
				graphics.fill(ellipse);
				return;
			case PATCH :
				graphics.setColor(Color.MAGENTA);
				var rectangle = new Rectangle2D.Float(x-5, y-5, 10, 10);
				graphics.fill(rectangle);
			default : return;
			
    		}
	      });
	}

	@Override
	public String toString() {
		switch (_status) {
		case PATCH : return "x";
		case BUTTON: return "0";
		default : return "|";
		}
	}
}