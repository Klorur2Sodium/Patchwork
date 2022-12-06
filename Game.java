package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

/**
 * This class contains the main loop of the game.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Game {
	private final TimeBoard _timeBoard;
	private final PlayerHandler _playerHandler;
	private final PieceHandler _pieceHandler;
	private final String _chosenVersion;

	/**
	 * Constructs a new Game object with the given non null TimeBoard,
	 * PlayerHandler, PieceHandler and Version.
	 * 
	 * @param timeBoard : the time board
	 * @param playerHandler : the player handler
	 * @param pieceHandler : the piece handler
	 * @param version : the given version of the game
	 */
	public Game(TimeBoard timeBoard, PlayerHandler playerHandler, PieceHandler pieceHandler, String version) {
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(playerHandler);
		Objects.requireNonNull(pieceHandler);
		Objects.requireNonNull(version);

		_timeBoard = timeBoard;
		_playerHandler = playerHandler;
		_pieceHandler = pieceHandler;
		_chosenVersion = version;
	}

	/**
	 * Main loop of the game, contains all the part of a turn and displays all the
	 * required information that the player must know to play the game.
	 * 
	 * @param scanner : a scanner
	 */
	public void playingPhase(Scanner scanner) {
		displayGame();

		int playerChoice;
		while (!_playerHandler.checkEndOfGame(_timeBoard.getSize())) {
			playerChoice = _playerHandler.getCurrent().buyingPhase(scanner, _pieceHandler);

			if (playerChoice == 0) {
				_playerHandler.getCurrent().skipTurn(scanner, _playerHandler.distanceBetweenPlayers() + 1, _timeBoard,
						_chosenVersion);
			} else {
				_playerHandler.getCurrent().buyPiece(_pieceHandler.getPiece(playerChoice - 1), scanner, _timeBoard,
						_chosenVersion);
				_pieceHandler.remove(_pieceHandler.getPiece(playerChoice - 1));
				_pieceHandler.moveNeutralPawn(playerChoice - 1);
				if (_chosenVersion.equals("a")) {
					_playerHandler.updateSpecialTile();
				}
			}
			if (!_playerHandler.checkEndOfGame(_timeBoard.getSize())) {
				_playerHandler.updateCurrentPlayer();
				_timeBoard.demarcateTurns();
				_timeBoard.displayTimeBoard(false, _playerHandler.getCurrent().getPosition());
				_pieceHandler.display(false);
			}
		}
		_playerHandler.displayWinner();
	}

	/**
	 * Displays the first turn with the captions.
	 */
	private void displayGame() {
		_timeBoard.displayTimeBoard(true, 0);
		_pieceHandler.display(true);
		System.out.println("\nlet's go !!\n");
	}
}
