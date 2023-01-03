package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.ScreenInfo;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

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
	private final Constants _chosenVersion;

	/**
	 * Constructs a new Game object with the given non null TimeBoard,
	 * PlayerHandler, PieceHandler and Version.
	 * 
	 * @param timeBoard : the time board
	 * @param playerHandler : the player handler
	 * @param pieceHandler : the piece handler
	 * @param version : the given version of the game
	 */
	public Game(TimeBoard timeBoard, PlayerHandler playerHandler, PieceHandler pieceHandler, Constants version) {
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

		Constants playerChoice;
		while (!_playerHandler.checkEndOfGame(_timeBoard.getSize())) {
			playerChoice = _playerHandler.getCurrent().buyingPhase(scanner, _pieceHandler);

			if (playerChoice == Constants.SKIP) {
				_playerHandler.getCurrent().skipTurn(scanner, _playerHandler.distanceBetweenPlayers() + 1, _timeBoard,
						_chosenVersion);
			} else {
				_playerHandler.getCurrent().buyPiece(_pieceHandler.getPiece(playerChoice.getValue()), scanner, _timeBoard,
						_chosenVersion);
				_pieceHandler.remove(_pieceHandler.getPiece(playerChoice.getValue()));
				_pieceHandler.moveNeutralPawn(playerChoice.getValue());
				if (_chosenVersion == Constants.PHASE2) {
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
	
	void draw() {	
		Application.run(Color.LIGHT_GRAY, context -> {
			ScreenInfo screenInfo = context.getScreenInfo();
		    float width = screenInfo.getWidth();
		    float height = screenInfo.getHeight();
			      context.renderFrame(graphics -> {
			        graphics.setColor(Color.LIGHT_GRAY);
			        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
			      });

			      _timeBoard.draw(context, width, 0);
		    	  _pieceHandler.draw(context, 3*(width/4), width, height);
		    	  _playerHandler.draw(context, 10, Constants.BOX_SIZE.getValue() + Constants.BOX_SIZE.getValue()/2);
			      while (true) {
			    	  Event event = context.pollOrWaitEvent(10);
			    	  if (event == null) {  // no event
				          continue;
				      }
			    	  Action action = event.getAction();
			          if (action == Action.KEY_PRESSED || action == Action.KEY_RELEASED) {
				          System.out.println("abort abort !");
				          context.exit(0);
				          return;
			          }
			      }
			    });  
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
