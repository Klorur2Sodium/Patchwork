package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.ApplicationContext;

import java.awt.Color;
import java.awt.Graphics2D;
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
	private final Menu _menu;

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
		_menu = new Menu();
	}

	/**
	 * Main loop of the game, contains all the part of a turn and displays all the
	 * required information that the player must know to play the game.
	 * 
	 * @param scanner : a scanner
	 */
	public void play(Scanner scanner) {
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
	
	public void placingPhase(Player player, Piece piece, ApplicationContext context) {
		int x = 0, y = 0;
		final int tx = x;
		final int ty = y;
		final Piece temp = piece;
		context.renderFrame(graphics -> {
			_playerHandler.cleanSpace(graphics);
			_menu.pieceMenu(graphics);
			_playerHandler.draw(graphics);
			temp.draw(graphics);
			player.getQuiltboard().drawPiece(graphics, temp, tx, ty);
		});
		while(true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {  // no event
		          continue;
		      }
			Action action = event.getAction();
			if (action == Action.KEY_PRESSED) {
				if (event.getKey().equals(KeyboardKey.LEFT) && x > 0) {
					x--;
				} else if (event.getKey().equals(KeyboardKey.RIGHT) && (x + piece.getXSize()) < Constants.GRID_SIZE.getValue()) {
					x++;
				}
				if (event.getKey().equals(KeyboardKey.UP) && y > 0) {
					y--;
				} else if (event.getKey().equals(KeyboardKey.DOWN) && (y + piece.getYSize()) < Constants.GRID_SIZE.getValue()) {
					y++;
				}
				if (event.getKey().equals(KeyboardKey.F)) {
					piece = piece.flip();
				} else if (event.getKey().equals(KeyboardKey.R)) {
					piece = piece.reverse();
				} else if (event.getKey().equals(KeyboardKey.Q)) {
					return;
				} else if (event.getKey().equals(KeyboardKey.S)) {
					if (player.addPieceToGrid(piece, y, x)) {
						piece = player.buy(piece, _timeBoard);
						_pieceHandler.remove(temp);
						if (piece != null) {
							continue;
						}
						return;
					}
				}
				final Piece moved = piece;
				final int xx = x;
				final int yy = y;
				context.renderFrame(graphics -> {
					_playerHandler.cleanSpace(graphics);
					_menu.pieceMenu(graphics);
					_playerHandler.draw(graphics);
					player.getQuiltboard().drawPiece(graphics, moved, xx, yy);
				});
			}
		}
	}

	public void play() {
		Application.run(Color.LIGHT_GRAY, context -> {
			ScreenInfo screenInfo = context.getScreenInfo();
		    float width = screenInfo.getWidth();
		    float height = screenInfo.getHeight();
		    float pieceHandlerPos =  4*(width/5);
		    float quiltBoardPos = Constants.BOX_SIZE.getValue() + Constants.BOX_SIZE.getValue()/2;
		    int pieceNumber = 0;
		  
		    _menu.SetGraphicalProperties(5, Constants.BOX_SIZE.getValue() + 87, width/6, height -  Constants.BOX_SIZE.getValue()-100);
		    
		      while (!_playerHandler.checkEndOfGame(_timeBoard.getSize())) {
		    	  final int toto = pieceNumber;
		    	  boolean played = false;
		    	  context.renderFrame(graphics -> draw(graphics, height, width, toto, pieceHandlerPos, quiltBoardPos));
		    	  Event event = context.pollOrWaitEvent(10);
		    	  if (event == null) {  // no event
			          continue;
			      }
		    	  Action action = event.getAction();
		    	  if (action == Action.KEY_PRESSED) {
		    		  if(event.getModifiers().contains(Event.Modifier.CTRL)) {
		    			  if(event.getKey().equals(KeyboardKey.S) && pieceNumber >= 0) { // trying to buy a piece
		    				  // need to add a message if the player is trying to buy a piece > 3
		    				  var piece = _pieceHandler.getPiece(pieceNumber);
		    				  if (_playerHandler.getCurrent().canBuyPiece(piece)) {
		    					  placingPhase(_playerHandler.getCurrent(), piece, context);
		    					  _pieceHandler.moveNeutralPawn(pieceNumber);
		    					  _pieceHandler.setDisplay(false);
		    					  pieceNumber = 0;
		    					  played = true;
		    				  }
		    			  } else if (event.getKey().equals(KeyboardKey.P)) { // displays the pieceHandler
		    				  _pieceHandler.setDisplay(true);
				        	  pieceNumber = 0;
		    			  } else if (event.getKey().equals(KeyboardKey.Q)) { // exit
		    				  context.exit(0);
					          return;
		    			  }
		    		  }
		    		  else if (event.getKey().equals(KeyboardKey.RIGHT)) {
		    			  pieceNumber++;
		    			  continue;	  
		    		  } else if (event.getKey().equals(KeyboardKey.LEFT) && pieceNumber > 0) {
		    			  pieceNumber--;
		    			  continue;
		    		  } else if (event.getKey().equals(KeyboardKey.S)){
		    			  _pieceHandler.setDisplay(false);
		    		  } else if (event.getKey().equals(KeyboardKey.SPACE)) {
		    			  Piece patch = null;
		    			  patch = _playerHandler.getCurrent().skipTurn(_playerHandler.distanceBetweenPlayers() + 1, _timeBoard);
		    			  if (patch != null) {
		    				  placingPhase(_playerHandler.getCurrent(), patch, context);
		    			  }
		    			  played = true;
		    		  }
		    		  if (played) {
		    			  _playerHandler.updateSpecialTile();
			    		  _playerHandler.updateCurrentPlayer();
		    		  }
		    	  }
		      }
		      context.renderFrame(graphics -> _playerHandler.drawVictory(graphics, height, width));
		});
	}
			
	public void draw(Graphics2D graphics, float height, float width, int pieceNumber, float pieceHandlerPos, float quiltBoardPos) {
		graphics.setColor(Color.LIGHT_GRAY);
        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
        _timeBoard.SetGraphicalProperties(0,  0, width, Constants.BOX_SIZE.getValue());
        _timeBoard.draw(graphics);
        _pieceHandler.SetGraphicalProperties(pieceHandlerPos, Constants.BOX_SIZE.getValue(), width, height);
	  	_pieceHandler.draw(graphics);
	  	_playerHandler.SetGraphicalProperties(10, quiltBoardPos, pieceHandlerPos, height - Constants.BOX_SIZE.getValue());
	  	_playerHandler.draw(graphics);
	  	_menu.draw(graphics);
		if (_pieceHandler.getDisplay()) {
			_pieceHandler.action(graphics, height, width, pieceNumber);
		}
		
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
