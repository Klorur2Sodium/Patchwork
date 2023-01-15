package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the information about a box, it's type (_status), 
 * the special status it can have
 * and the players on it 
 * 
 * @author FRAIZE Victor
 * @author COUSSON Sophie
 */
public class Box extends GraphicalObject {
	private Constants _status;
	private final ArrayList<IOpponent> _players = new ArrayList<>();
	private SpecialeBox _special = null;

	/**
	* The method is the constructor it takes a char status and put it on the _status
	*
	* @param status : status of the box
	*/
	public Box(char status) {
		Objects.requireNonNull(status);
		// special will be null in most cases
		switch (status) {
			case '|' -> _status = Constants.EMPTY;
			case '0' -> _status = Constants.BUTTON;
			case 'x' -> _status = Constants.PATCH;
			default -> throw new IllegalArgumentException("status must be equal to |, 0 or x");
		}
	}
	
	/**
	 * the function sets a value on the field _special
	 * @param special : the special status
	 */
	public void setSpecial(SpecialeBox special) {
		Objects.requireNonNull(special);
		_special = special;
	}
	
	/**
	 * the function return the special status of the box
	 * @return _special : the special status
	 */
	public SpecialeBox getSpecial() {
		return _special;
	}
	
	/**
	 * The method returns the players List
	 * 
	 * @return a List of IOpponent
	 */
	public List<IOpponent> getPlayers() {
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
	 * return a boolean which indicates that there is at least one player on the box
	 * @return boolean
	 */
	public boolean hasPlayer() {
		return _players.size() > 0;
	}
	
	/**
	 * the function empty status
	 */
	public void emptyStatus() {
		_status =  Constants.EMPTY;
	}

	/**
 	 * The method adds a player to the List of Players
 	 * 
 	 * @param iOpponent to add
 	 */
	public void add(IOpponent iOpponent) {
		Objects.requireNonNull(iOpponent);
		_players.add(iOpponent);
	}

	/**
 	 * The method removes a player form the List of players
 	 * 
 	 * @param opponent to remove
 	 */
 	public void remove(IOpponent opponent) {
 		Objects.requireNonNull(opponent);
 		_players.remove(opponent);
 	}

 	/**
 	 * The method returns the last player added to the List of players
 	 * 
 	 * @return a Player
 	 */
 	public IOpponent getPlayer() {
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
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(version);

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
	 * The function indactes if the box contains the win/loose option
	 * if the return is negativ the player looses half otherwise he wins
	 * @return 0 if the box doesn't contains the special tile
	 */
	public int winLose() {
		if (_special != null && _special == SpecialeBox.WIN_LOSE) {
			if (Math.random() < 0.5) {
				return 1;
			}
			return -1;
		}
		return 0;
	}
	
	/**
	 * The function return true if the box has the special type FREEZE
	 * otherwise false 
	 * @return boolean
	 */
	public boolean freeze() {
		return _special != null && _special == SpecialeBox.FREEZE;
	}
	
	/**
	 * The function returns true if the box has the special type DOUBLE
	 * otherwise false
	 * @return boolean
	 */
	public boolean isDouble() {
		return _special != null && _special == SpecialeBox.DOUBLE;
	}
	
	/**
	 * The function returns true if the box has the special type SWITCH_POSITION
	 * otherwise false
	 * @return boolean
	 */
	public boolean isSwitch() {
		return _special != null && _special == SpecialeBox.SWITCH_POSITION;
	}
	
	/**
	 * The function return true if the box has the special type DRAW
	 * @return boolean
	 */
	public boolean isDraw() {
		if (_special != null && _special == SpecialeBox.DRAW) {
			_special = null;
			return true;
		}
		return false;
	}
	
	/**
	 * the function returns true if the box has the special type CHANCE
	 * @return boolean
	 */
	public boolean isChance() {
		return _special != null && _special == SpecialeBox.CHANCE;
	}
	
	/**
	 * The function returns true if the box has the special type SWITCH_BOARD
	 * @return boolean
	 */
	public boolean isSwitchBoard() {
		if (_special != null && _special == SpecialeBox.SWITCH_BOARD) {
			_special = null;
			return true;
		}
		return false;
	}
	
	/**
	 * The function draws the statud of the box (a circle for a button or a 
	 * square for a patch)
	 * @param graphics
	 */
	private void drawStatus(Graphics2D graphics) {
		switch(_status) {
		case BUTTON : 
			graphics.setColor(Color.CYAN);
			Ellipse2D.Float ellipse = new Ellipse2D.Float(topLeftX - 5, topLeftY - 5, width, height);
			graphics.fill(ellipse);
			break;
		case PATCH :
			graphics.setColor(Color.MAGENTA);
			var rectangle = new Rectangle2D.Float(topLeftX - 5, topLeftY - 5, width, height);
			graphics.fill(rectangle);
		default : break;
		}
	}
	
	/**
	 * The function draws the special status of the box (if it has one)
	 * @param graphics
	 */
	private void drawSpecial(Graphics2D graphics) {
		if (_special == null) {return;}
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("default", Font.BOLD, 12));
		switch(_special) {
		case WIN_LOSE : 
			graphics.drawString("Win", topLeftX + width/2, topLeftY + height/2-5);
			graphics.drawString("Lose", topLeftX + width/2, topLeftY + height/2+5);
			return;
		case FREEZE :
			graphics.drawString("Freeze", topLeftX + width/2, topLeftY + height/2);
			return;
		case DOUBLE :
			graphics.drawString("Double", topLeftX + width/2, topLeftY + height/2);
			return;
		case SWITCH_POSITION :
			graphics.drawString("Switch", topLeftX + width/2, topLeftY + height/2-5);
			graphics.drawString("Pos", topLeftX + width/2, topLeftY + height/2+5);
			return;
		case CHANCE :
			graphics.drawString("Chance", topLeftX + width/2, topLeftY + height/2-5);
			return;
		case SWITCH_BOARD :
			graphics.drawString("Forced", topLeftX + width/2, topLeftY + height/2-5);
			graphics.drawString("Board", topLeftX + width/2, topLeftY + height/2+5);
			return;
		case DRAW :
			graphics.drawString("Draw", topLeftX + width/2, topLeftY + height/2);
			return;
		default : return;
		}
	}
	

	@Override
	protected void onDraw(Graphics2D graphics) {
		drawStatus(graphics);
		drawSpecial(graphics);
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