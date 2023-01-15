package fr.uge.patchwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.awt.Font;

/**
 * The class stores an array of players and handles the current player, the end
 * of the game and the winner.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class OpponentHandler extends GraphicalObject {
	private final IOpponent[] _opponents;
	private int _current;
	private boolean _specialTileRemaining;

	/**
	 * Constructs a new PlayerHandler.
	 * 
	 * @param opponents : the opponents
	 * @param special : the special tile
	 */
	public OpponentHandler(IOpponent[] opponents, boolean special) {
		Objects.requireNonNull(opponents);
		_opponents = opponents;
		_specialTileRemaining = special;
		_current = 0;
	}

	/**
	 * Returns the player located at the given index.
	 * 
	 * @param i : index
	 * @return player
	 */
	public IOpponent getOpponentIndex(int i) {
		if (i < 0 || i > 1) {
			throw new IllegalArgumentException("there is only two players");
		}
		return _opponents[i];
	}

	/**
	 * Returns the human player in solo mode.
	 * 
	 * @return A player class
	 */
	public IOpponent getHumanPlayer() {
		return _opponents[0];
	}

	/**
	 * Returns the current player.
	 * 
	 * @return current player
	 */
	public IOpponent getCurrent() {
		return _opponents[_current];
	}

	/**
	 * Returns true if there is a special tile left.
	 * 
	 * @return boolean
	 */
	public boolean specialTileRemaining() {
		return _specialTileRemaining;
	}

	/**
	 * Changes the state of _specialTile if the special tile is given to a player
	 */
	public void updateSpecialTile() {
		if (specialTileRemaining() && _opponents[_current].updateSpeTile()) {
			System.out.println();
			_specialTileRemaining = false;
		}
	}

	/**
	 * Updates the current player.
	 */
	public void updateCurrentPlayer(TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		if (timeBoard.isSwitchBox(_opponents[_current].getPosition())) {
			switchPlayers(timeBoard, _opponents[_current].getPosition(), _opponents[(_current + 1) % 2].getPosition());
		}
		if (timeBoard.isSwitchBoardBox(_opponents[_current].getPosition())) {
			switchBoard();
		}
		if (_opponents[0].getPosition() > _opponents[1].getPosition()) {
			_current = 1;
		} else if (_opponents[0].getPosition() < _opponents[1].getPosition()) {
			_current = 0;
		}
		WinLose(timeBoard);
	}

	/**
	 * The function returns true if the current player is on a box containing the
	 * special status CHANCE
	 * 
	 * @param timeBoard
	 * @return boolean
	 */
	public boolean hasCurrentPlayerChance(TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		return timeBoard.isChanceBox(_opponents[_current].getPosition());
	}

	/**
	 * the function switches the players positions
	 * 
	 * @param timeBoard
	 * @param positionCurrent
	 * @param positionNext
	 */
	private void switchPlayers(TimeBoard timeBoard, int positionCurrent, int positionNext) {
		timeBoard.getBoard().get(positionCurrent).remove(_opponents[_current]);
		timeBoard.getBoard().get(positionNext).add(_opponents[_current]);
		timeBoard.getBoard().get(positionCurrent).add(_opponents[(_current + 1) % 2]);
		timeBoard.getBoard().get(positionNext).remove(_opponents[(_current + 1) % 2]);
		_opponents[_current].setPosition(positionNext);
		_opponents[(_current + 1) % 2].setPosition(positionCurrent);
	}

	/**
	 * the function switch the players boards
	 */
	private void switchBoard() {
		var currentBoard = _opponents[_current].getQuiltboard();
		var otherBoard = _opponents[(_current + 1) % 2].getQuiltboard();
		_opponents[_current].setQuiltBoard(otherBoard);
		_opponents[(_current + 1) % 2].setQuiltBoard(currentBoard);
	}

	/**
	 * the function increments and decrement the correct player buttonCount if a
	 * player is on box WinLose
	 * 
	 * @param timeBoard
	 */
	private void WinLose(TimeBoard timeBoard) {
		var res = timeBoard.isWinLoseBox(_opponents[_current].getPosition());
		if (res == 1) {
			_opponents[_current].setButtons(_opponents[(_current + 1) % 2].getButton() / 2, true);
			_opponents[(_current + 1) % 2].setButtons(_opponents[(_current + 1) % 2].getButton() / 2, false);
		} else if (res == -1) {
			_opponents[_current].setButtons(_opponents[_current].getButton() / 2, false);
			_opponents[(_current + 1) % 2].setButtons(_opponents[_current].getButton() / 2, true);
		}
	}

	/**
	 * Returns the distance between the players.
	 * 
	 * @return distance
	 */
	public int distanceBetweenPlayers() {
		return Math.abs(_opponents[0].getPosition() - _opponents[1].getPosition());
	}

	/**
	 * Checks whether or not the game is finished
	 * 
	 * @param boardSize : size of the board
	 * @return boolean
	 */
	public boolean checkEndOfGame(int boardSize) {
		if (boardSize < 0) {
			throw new IllegalArgumentException("Invalid size");
		}
		return _opponents[0].getPosition() == _opponents[1].getPosition() && _opponents[0].getPosition() == (boardSize - 1);
	}

	/**
	 * Returns the player with the greatest score.
	 * 
	 * @return best player
	 */
	private IOpponent getVictoriousPlayer() {
		return (_opponents[0].getScore() > _opponents[1].getScore()) ? _opponents[0] : _opponents[1];
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
	 * 
	 * @param context
	 * @param topX
	 * @param topY
	 */
	@Override
	protected void onDraw(Graphics2D graphics) {
		if (_opponents[_current].getClass().equals(Player.class)) {
			_opponents[_current].SetGraphicalProperties(topLeftX, topLeftY, width, height);
			_opponents[_current].draw(graphics);
		}
	}

	/**
	 * Displays the automa' stats under the menu.
	 * 
	 * @param graphics : object that calls the graphic functions.
	 */
	public void drawAutomaStats(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
		if (_opponents[_current].getClass().equals(Automa.class)) {
			_opponents[1].SetGraphicalProperties(20, 560, width, height);
			_opponents[1].draw(graphics);
		}
	}

	/**
	 * 
	 * @param graphics : object that calls the graphic functions.
	 */
	public void cleanSpace(Graphics2D graphics) {
		Objects.requireNonNull(graphics);
		graphics.setColor(Color.LIGHT_GRAY);
		var rect = new Rectangle2D.Float(0, Constants.BOX_SIZE.getValue() + 10, width - 10, height);
		graphics.fill(rect);
	}

	/**
	 * Draws the winning screen.
	 * 
	 * @param graphics : object that calls the graphic functions.
	 * @param wHeight  : window height.
	 * @param wWidth   : window width.
	 */
	public void drawVictory(Graphics2D graphics, float wHeight, float wWidth) {
		Objects.requireNonNull(graphics);
		var winner = getVictoriousPlayer();
		var text = "Player " + winner.getName() + " you won this game with " + winner.getScore();
		graphics.setColor(Color.LIGHT_GRAY);
		var rect = new Rectangle2D.Float(0, 0, wWidth, wHeight);
		graphics.fill(rect);
		graphics.setColor(Color.BLACK);
		graphics.setFont(new Font("default", Font.BOLD, 50));
		graphics.drawString(text, wWidth / 2 - graphics.getFontMetrics().stringWidth(text) / 2, wHeight / 2);
	}

}
