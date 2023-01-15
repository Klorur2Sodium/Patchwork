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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * This class contains the main loop of the game.
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Game {
	private final TimeBoard _timeBoard;
	private final CardHandler _cardHandler;
 	private final OpponentHandler _opponentHandler;
	private final PieceHandler _pieceHandler;
	private final Constants _chosenVersion;
	private final Menu _menu;

	/**
	 * Constructs a new Game object with the given non null TimeBoard,
 	 * PlayerHandler, PieceHandler and Version.
 	 * 
 	 * @param timeBoard     : the time board
 	 * @param playerHandler : the player handler
 	 * @param pieceHandler  : the piece handler
 	 * @param version       : the given version of the game
 	 */
 	public Game(TimeBoard timeBoard, OpponentHandler playerHandler, PieceHandler pieceHandler, CardHandler cardHandler,
 			Constants version) {
 		Objects.requireNonNull(timeBoard);
 		Objects.requireNonNull(playerHandler);
 		Objects.requireNonNull(pieceHandler);
 		Objects.requireNonNull(version);

 		_timeBoard = timeBoard;
 		_cardHandler = cardHandler;
 		_opponentHandler = playerHandler;
 		_pieceHandler = pieceHandler;
 		_chosenVersion = version;
 		_menu = new Menu();
 	}
	
	
	/**
	 * the function launches the correct version of the game
	 */
	public void launch() {
		switch (_chosenVersion) {
 		case PHASE1:
 			play(new Scanner(System.in));
 			return;
 		case PHASE2:
 			play(new Scanner(System.in));
 			return;
 		case PHASE3:
 			play();
 			return;
 		case PHASE4:
 			_cardHandler.initPile();
 			play();
 			return;
 		case PHASE5:
			_timeBoard.addSpecialBox();
			play();
		default:
		return;
		}
	}


	/**
	 * Main loop of the game, contains all the part of a turn and displays all the
	 * required information that the player must know to play the game.
	 * 
	 * 
 	 * @param scanner : a scanner
 	 */
 	private void play(Scanner scanner) {
 		displayGame();
 		Constants playerChoice;
 		while (!_opponentHandler.checkEndOfGame(_timeBoard.getSize())) {
 			playerChoice = _opponentHandler.getCurrent().buyingPhase(scanner, _pieceHandler);

 			if (playerChoice == Constants.SKIP) {
 				_opponentHandler.getCurrent().skipTurn(scanner, _opponentHandler.distanceBetweenPlayers() + 1, _timeBoard,
 						_chosenVersion);
 			} else {
 				_opponentHandler.getCurrent().buyPiece(_pieceHandler.getPiece(playerChoice.getValue()), scanner, _timeBoard,
 						_chosenVersion);
 				_pieceHandler.remove(_pieceHandler.getPiece(playerChoice.getValue()));
 				_pieceHandler.moveNeutralPawn(playerChoice.getValue());
 				if (_chosenVersion == Constants.PHASE2) {
 					_opponentHandler.updateSpecialTile();
 				}
 			}
 			if (!_opponentHandler.checkEndOfGame(_timeBoard.getSize())) {
 				_opponentHandler.updateCurrentPlayer(_timeBoard);
 				_timeBoard.demarcateTurns();
 				_timeBoard.displayTimeBoard(false, _opponentHandler.getCurrent().getPosition());
 				_pieceHandler.display(false);
 			}
 		}
 		_opponentHandler.displayWinner();
 	}
	
	
	/**
	 * The function allows the player to place the piece he selected on his quiltBoard
	 * @param player the player that is placing the piece
	 * @param piece the piece placed 
	 * @param context the context were to draw
	 */
	private int placingPhase(IOpponent player, Piece piece, ApplicationContext context) {
		int x = 0, y = 0;
		Piece initial = piece;
		drawPlacingPhase(context, piece, player, x, y, true);
		while(true) {
			Event event = context.pollOrWaitEvent(10);
			if (event == null) {  // no event
		          continue;
		    }
			Action action = event.getAction();
			if (action == Action.KEY_PRESSED) {
				if (event.getKey().equals(KeyboardKey.LEFT) && y > 0) {
					y--;
				} else if (event.getKey().equals(KeyboardKey.RIGHT) && (y + piece.getXSize()) < Constants.GRID_SIZE.getValue()) {
					y++;
				}
				if (event.getKey().equals(KeyboardKey.UP) && x > 0) {
					x--;
				} else if (event.getKey().equals(KeyboardKey.DOWN) && (x + piece.getYSize()) < Constants.GRID_SIZE.getValue()) {
					x++;
				}
				if (event.getKey().equals(KeyboardKey.F)) {
					piece = piece.flip();
					y = xValueAfterFlip(piece, y);
					x = yValueAfterFlip(piece, x);
				} else if (event.getKey().equals(KeyboardKey.R)) {
					piece = piece.reverse();
				} else if (event.getKey().equals(KeyboardKey.Q)) {
					return 0;
				} else if (event.getKey().equals(KeyboardKey.S)) {
					if (player.addPieceToGrid(piece, y, x)) {
						piece = player.recoverPiece(piece, _timeBoard);
						if (_timeBoard.isDrawBox(_opponentHandler.getCurrent().getPosition())) {
							piece = chooseOptionalPiece(context, _opponentHandler.getCurrent());
							if (piece != null) {
								placingPhase(_opponentHandler.getCurrent(), piece, context);
							}
						}
						_pieceHandler.remove(initial);
						if (piece != null) {
							placingPhase(_opponentHandler.getCurrent(), piece, context);
						}
						return 1;
					}
				}
				drawPlacingPhase(context, piece, player, x, y, false);
			}
		}
	}
	
 	private void automaTurn() {
 		Piece piece;
 		var automa = _opponentHandler.getCurrent();
 		var card = _cardHandler.drawCard();

 		var listBuyablePieces = _pieceHandler.getSelectablePieces();

 		piece = automa.buyingPhase(listBuyablePieces, _opponentHandler.getHumanPlayer().getPosition(), automa.getPosition(), card, _timeBoard);
 		if (piece != null) {
 			_pieceHandler.moveNeutralPawn(_pieceHandler.getSelectablePieces().indexOf(piece));
 			_pieceHandler.remove(piece);
 		}
 		//test(card, listBuyablePieces, piece, automa.getPosition(), _opponentHandler.getHumanPlayer().getPosition());

 		_opponentHandler.updateSpecialTile();
 		_opponentHandler.updateCurrentPlayer(_timeBoard);
 	}
	
	
	/**
	 * The function calls the function to add the piece to the grid and 
	 * to do the necessary changes after a piece has been purchased
	 * return a boolean that indicates if the piece was bought
	 * @param context
	 * @param pieceNumber
	 * @return boolean
	 */
	private boolean buy(ApplicationContext context, int pieceNumber) {
		var piece = _pieceHandler.getPiece(pieceNumber);
		if (_opponentHandler.getCurrent().canBuyPiece(piece)) {
			if (pieceNumber < 3 || pieceNumber < 12 && _opponentHandler.hasCurrentPlayerChance(_timeBoard))  {
				if (placingPhase(_opponentHandler.getCurrent(), piece, context) == 1) {
					_pieceHandler.moveNeutralPawn(pieceNumber);
					_pieceHandler.setDisplay(false);
				} else {
					_pieceHandler.setDisplay(false);
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	
	
	private int eventHandeling(ApplicationContext context, Event event, boolean played, int pieceNumber) {
		if(event.getModifiers().contains(Event.Modifier.CTRL)) {
			if(event.getKey().equals(KeyboardKey.S) && pieceNumber >= 0) { // trying to buy a piece
				// need to add a message if the player is trying to buy a piece > 3
				if (buy(context, pieceNumber)) {
					played = true;
					pieceNumber = 0;
				 }
			} else if (event.getKey().equals(KeyboardKey.P)) { // displays the pieceHandler
				_pieceHandler.setDisplay(true);
	        	return 0;
			} else if (event.getKey().equals(KeyboardKey.Q)) { // exit
				context.exit(0);
		        return 0;
			}
		} else if (event.getKey().equals(KeyboardKey.RIGHT)) {
			return pieceNumber + 1;
		} else if (event.getKey().equals(KeyboardKey.LEFT) && pieceNumber > 0) {
			return pieceNumber - 1;
		} else if (event.getKey().equals(KeyboardKey.S)){
			_pieceHandler.setDisplay(false);
		} else if (event.getKey().equals(KeyboardKey.SPACE)) {
			Piece patch = null;
			patch = _opponentHandler.getCurrent().skipTurn(_opponentHandler.distanceBetweenPlayers() + 1, _timeBoard);
			if (_timeBoard.isDrawBox(_opponentHandler.getCurrent().getPosition())) {
				var piece = chooseOptionalPiece(context, _opponentHandler.getCurrent());
				if (piece != null) {
					placingPhase(_opponentHandler.getCurrent(), piece, context);
				}
			}
			if (patch != null) {
				placingPhase(_opponentHandler.getCurrent(), patch, context);
			}
			played = true;
		}
		if (played) {
			_opponentHandler.updateSpecialTile();
			_opponentHandler.updateCurrentPlayer(_timeBoard);
			return 0;
		}
		return pieceNumber;  
	}

	/**
	 * Main loop of the game, contains all the part of a turn and displays all the
	 * required information that the player must know to play the game.
	 * 
	 * used for the graphic version
	 */
	private void play() {
		Application.run(Color.LIGHT_GRAY, context -> {
			ScreenInfo screenInfo = context.getScreenInfo();
		    float width = screenInfo.getWidth();
		    float height = screenInfo.getHeight();
		    float pieceHandlerPos =  4*(width/5);
		    float quiltBoardPos = Constants.BOX_SIZE.getValue() + Constants.BOX_SIZE.getValue()/2;
		    int pieceNumber = 0;
		    _menu.SetGraphicalProperties(5, Constants.BOX_SIZE.getValue() + 87, width/6, height -  Constants.BOX_SIZE.getValue()-100);
		    
		      while (!_opponentHandler.checkEndOfGame(_timeBoard.getSize())) {
		    	  final int toto = pieceNumber;
		    	  boolean played = false;
		    	  context.renderFrame(graphics -> draw(graphics, height, width, toto, pieceHandlerPos, quiltBoardPos));
		    	  Event event = context.pollOrWaitEvent(10);
		    	  if (event == null) {  // no event
			          continue;
			      }
		    	  Action action = event.getAction();
		    	  if (action == Action.KEY_PRESSED) {
		    		  if (_opponentHandler.getCurrent().getClass().equals(Player.class)) {
		    			  pieceNumber = eventHandeling(context, event, played, pieceNumber);
		    		  } else {
		    			  automaTurn();
		    		  }
		    	  }
		      }
		      context.renderFrame(graphics -> _opponentHandler.drawVictory(graphics, height, width));
		      context.exit(1);
		});
	}
	
	
	/**
	 * The function draw the different options available if the player
	 * is in the special box DRAW 
	 * @param graphics
	 * @param height
	 * @param width
	 * @param pieces
	 */
	private void drawOptions(Graphics2D graphics, float height, float width, Piece pieces[]) {
		int size = 125;
	  _menu.drawMenu(graphics);
	  graphics.setColor(Color.GRAY);
	  var rec = new Rectangle2D.Float(width/2- width/10, height/2-width/5, size*3, size*3);
	  graphics.fill(rec);
	  graphics.setColor(Color.BLACK);
	  graphics.draw(rec);
	  for (var i = 0; i < 3; i++) {
		  graphics.draw(new Line2D.Double(width/2- width/10, i*size + height/2-width/5, width/2- width/10 + size*3, i*size + height/2-width/5));
		  graphics.draw(new Line2D.Double(width/2- width/10 + i*size, height/2-width/5, width/2- width/10 + i*size, size*3+height/2-width/5));
	  }
	  float x = width/2- width/10;
	  float y = height/2-width/5;
	  for (var i = 0; i < 9; i++) {
		  if (i%3==0 && i != 0) {
			  x = width/2- width/10;
			  y+=size;
		  }
		  pieces[i].SetGraphicalProperties(x+10, y+10, Constants.PIECE_SQUARE.getValue());
		  pieces[i].onDraw(graphics);
		  x += size;
	  }
	}
	
	/**
	 * The function allows the player to choose the piece he wants to add to 
	 * his grid (4blocs max) 
	 * @param context
	 * @param player
	 * @return the piece chosen
	 */
	public Piece chooseOptionalPiece(ApplicationContext context, IOpponent player) {
		ScreenInfo screenInfo = context.getScreenInfo();
	    float width = screenInfo.getWidth();
	    float height = screenInfo.getHeight();
	    float size = 125;
	    var pieces = _pieceHandler.cubePieces();
	    _menu.SetGraphicalProperties(5, Constants.BOX_SIZE.getValue() + 87, width/6, height -  Constants.BOX_SIZE.getValue()-100);
	    int xx = 0, yy = 0;
	   context.renderFrame(graphics ->
	   {
		  drawOptions(graphics, height, width, pieces);
		  graphics.setColor(Color.MAGENTA);
		  var rectangle = new Rectangle2D.Float(width/2- width/10, height/2-width/5, size, size);
		  graphics.draw(rectangle);
	   });
	  while(true) {
		  Event event = context.pollOrWaitEvent(10);
    	  if (event == null) {  // no event
	          continue;
	      }
    	  Action action = event.getAction();
    	  if (action == Action.KEY_PRESSED) {
    		  if (event.getKey().equals(KeyboardKey.LEFT) && xx > 0) {
    			  xx--;
    		  } else if (event.getKey().equals(KeyboardKey.RIGHT) && xx < 3-1) {
    			  xx++;
    		  } else if (event.getKey().equals(KeyboardKey.UP) && yy > 0) {
    			  yy--;
    		  } else if (event.getKey().equals(KeyboardKey.DOWN) && yy < 3-1) {
    			  yy++;
    		  } else if (event.getKey().equals(KeyboardKey.Q)) {
    			  return null;
    		  } else if (event.getKey().equals(KeyboardKey.SPACE)) {
    			  return pieces[(int)(yy*3+xx)];
    		  }
    	  }
    	  var toto = xx;
		  var tutu = yy;
    	  context.renderFrame(graphics ->
		   {   
			  drawOptions(graphics, height, width, pieces);
			  graphics.setColor(Color.MAGENTA);
			  var rectangle = new Rectangle2D.Float(width/2- width/10 + toto * size, height/2-width/5 + tutu*size, size, size);
			  graphics.draw(rectangle);
		   });
	   }
	}
		
	/**
	 * The function draws on the graphics the game's element
	 * @param graphics 
	 * @param height window height
	 * @param width window width
	 * @param pieceNumber the number of the piece displayed
	 * @param pieceHandlerPos the starting position of the pieceHandler drawing
	 * @param quiltBoardPos the starting position of the quiltBoard drawing
	 */	
	 private void draw(Graphics2D graphics, float height, float width, int pieceNumber, float pieceHandlerPos,
	 			float quiltBoardPos) {
		graphics.setColor(Color.LIGHT_GRAY);
        graphics.fill(new  Rectangle2D.Float(0, 0, width, height));
        _timeBoard.SetGraphicalProperties(0,  0, width, Constants.BOX_SIZE.getValue());
        _timeBoard.draw(graphics);
        _pieceHandler.SetGraphicalProperties(pieceHandlerPos, Constants.BOX_SIZE.getValue(), width, height);
	  	_pieceHandler.draw(graphics);
	  	_opponentHandler.SetGraphicalProperties(10, quiltBoardPos, pieceHandlerPos, height - Constants.BOX_SIZE.getValue());
	  	_opponentHandler.draw(graphics);
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

	/**
	 * The function returns the value of y after the piece has flip
	 * @param piece the new piece
	 * @param y the current value of y
	 * @return the new value of y
	 */
	private int yValueAfterFlip(Piece piece, int y) {
		if (y + piece.getYSize() >= Constants.GRID_SIZE.getValue()) {
			return Constants.GRID_SIZE.getValue() - piece.getYSize();
		}
		return y;
	}

	/**
	 * The function returns the value of x after the piece has flip
	 * @param piece the new piece
	 * @param y the current value of x
	 * @return the new value of x
	 */
	private int xValueAfterFlip(Piece piece, int x) {
		if (x + piece.getXSize() >= Constants.GRID_SIZE.getValue()) {
			return Constants.GRID_SIZE.getValue() - piece.getXSize();
		}
		return x;
	}


	/**
	 * The function draws the current state of the player's timeBoard with the menu and the piece selected
	 * @param context 
	 * @param piece the piece selected
	 * @param player the players that plays
	 * @param x the coordinate on the board
	 * @param y the coordinate on the board
	 * @param drawPiece indicate if it's necessary to draw the piece twice
	 */
	private void drawPlacingPhase(ApplicationContext context, Piece piece, IOpponent player, int x, int y, boolean drawPiece) {
		context.renderFrame(graphics -> {
			_opponentHandler.cleanSpace(graphics);
			_menu.pieceMenu(graphics);
			_opponentHandler.draw(graphics);
			if (drawPiece) {piece.draw(graphics);}
			player.getQuiltboard().drawPiece(graphics, piece, y, x);
		});
	}

}
