package fr.uge.patchwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.awt.Font;

/**
 * The class stores an array of players and handles the current player, 
 * the end of the game and the winner.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class PlayerHandler extends GraphicalObject {
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
	public void updateCurrentPlayer(TimeBoard timeBoard) {
		if (timeBoard.isSwitchBox(_players[_current].getPosition())) {
			switchPlayers(timeBoard, _players[_current].getPosition(), _players[(_current + 1)%2].getPosition());
		}
		if (timeBoard.isSwitchBoardBox(_players[_current].getPosition())) {
			switchBoard();
		}
		if (_players[0].getPosition() > _players[1].getPosition()) {
			_current =  1;
        } else if (_players[0].getPosition() < _players[1].getPosition()) {
        	_current =  0;
        }
		WinLose(timeBoard);
    }
	
	/**
	 * The function returns true if the current player is on a box 
	 * containing the special status CHANCE
	 * @param timeBoard
	 * @return boolean
	 */
	public boolean hasCurrentPlayerChance(TimeBoard timeBoard) {
		return timeBoard.isChanceBox(_players[_current].getPosition());
	}
	
	/**
	 * the function switches the players positions
	 * @param timeBoard
	 * @param positionCurrent
	 * @param positionNext
	 */
	private void switchPlayers(TimeBoard timeBoard, int positionCurrent, int positionNext) {
		timeBoard.getBoard().get(positionCurrent).remove(_players[_current]);
		timeBoard.getBoard().get(positionNext).add(_players[_current]);
		timeBoard.getBoard().get(positionCurrent).add(_players[(_current+1)%2]);
		timeBoard.getBoard().get(positionNext).remove(_players[(_current+1)%2]);
		_players[_current].setPosition(positionNext);
		_players[(_current+1)%2].setPosition(positionCurrent);
	}
	
	/**
	 * the function switch the players boards
	 */
	private void switchBoard() {
		var currentBoard = _players[_current].getQuiltboard();
		var otherBoard = _players[(_current+1)%2].getQuiltboard();
		_players[_current].setQuiltBoard(otherBoard);
		_players[(_current+1)%2].setQuiltBoard(currentBoard);
	}
	
	/**
	 * the function increments and decrement the correct player buttonCount
	 * if a player is on box WinLose
	 * @param timeBoard
	 */
	private void WinLose(TimeBoard timeBoard) {
		var res = timeBoard.isWinLoseBox(_players[_current].getPosition()); 
		if (res == 1) {
			_players[_current].setButtons(_players[(_current+1)%2].getButton()/2, true);
			_players[(_current+1)%2].setButtons(_players[(_current+1)%2].getButton()/2, false);
		} else if (res == -1){
			_players[_current].setButtons(_players[_current].getButton()/2, false);
			_players[(_current+1)%2].setButtons(_players[_current].getButton()/2, true);
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
	
	@Override
	protected void onDraw(Graphics2D graphics) {
		_players[_current].SetGraphicalProperties(topLeftX, topLeftY, width, height);
		_players[_current].draw(graphics);
	}
	
	/**
	 * The function draws a grey rectangle on the window
	 * @param graphics
	 */
	public void cleanSpace(Graphics2D graphics) {
		graphics.setColor(Color.LIGHT_GRAY);
		var rect = new Rectangle2D.Float(0, Constants.BOX_SIZE.getValue()+10, width-10, height);
		graphics.fill(rect);
	}
	
	/**
	 * the function draw a message at the end of the game to indicate the winner
	 * @param graphics
	 * @param wHeight
	 * @param wWidth
	 */
	public void drawVictory(Graphics2D graphics, float wHeight, float wWidth) {
		var winner = getVictoriousPlayer();
		var text = "Player " + winner.getName() + " you won this game with " + winner.getScore();
		graphics.setColor(Color.LIGHT_GRAY);
		var rect = new Rectangle2D.Float(0, 0, wWidth, wHeight);
		graphics.fill(rect);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("default", Font.BOLD,50));
		graphics.drawString(text, wWidth/2 - graphics.getFontMetrics().stringWidth(text)/2, wHeight/2);
	}
	
}










