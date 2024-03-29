package fr.uge.patchwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class stores the information about the Player. It also handles 
 * all its action during the playing phase. Like moving, buying a piece etc...
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public final class Player extends GraphicalObject implements IOpponent {
	private QuiltBoard _quiltBoard;
	private int _buttonsCount;
	private final String _name;
	private int _wage;
	private Pawn _pawn;
	private int _position;
	private boolean _specialTile;

	/**
	 * Constructs a new Player with the given name and color
	 * 
	 * @param name : player's name
	 * @param color : player's color
	 */
	public Player(String name, String color) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(color);
		_name = name;
		_wage = 0;
		_buttonsCount = 5;
		_pawn = new Pawn(color);
		_position = 0;
		_specialTile = false;

		_quiltBoard = new QuiltBoard();
	}

	/**
	 * The function sets the player position to another position
	 * @param position ! new position.
	 */
	public void setPosition(int position) {
		if (position < 0) {
			throw new IllegalArgumentException("Invalid position");
		}
		_position = position;
	}
	
	/**
	 * Getter for the name of the player
	 * 
	 * @return name of the player
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * returns the number of buttons the player owns
	 * @return the buttonsCount
	 */
	public int getButton() {
		return _buttonsCount;
	}
	
	/**
	 * Getter for the pawn of the player
	 * 
	 * @return pawn of the player
	 */
	public Pawn getPawn() {
		return _pawn;
	}

	/**
	 * Getter for the position of the player
	 * 
	 * @return position of the player
	 */
	public int getPosition() {
		return _position;
	}

	/**
	 * Getter for the quilt board
	 * 
	 * @return the quilt board
	 */
	public QuiltBoard getQuiltboard() {
		return _quiltBoard;
	}
	
	/**
	 * Returns the score of the player
	 * 
	 * @return score
	 */
	public int getScore() {
		var partialScore = _buttonsCount - _quiltBoard.getEmpty() * 2;
		return (_specialTile) ? partialScore : partialScore + 7;
	}

	/**
	 * The function adds or retrieves nbButton to the buttonCount of the player
	 * @param nbButton : number of buttons to add/ retrieve
	 * @param add : indicates if we had or retrieve buttons
	 */
	public void setButtons(int nbButton, boolean add) {
		if (add) {
			_buttonsCount += nbButton;
		} else {
			_buttonsCount -= nbButton;
		}
	}
	
	/**
	 * The function sets the quiltBoard
	 * @param board : new quilt board
	 */
	public void setQuiltBoard(QuiltBoard board) {
		Objects.requireNonNull(board);
		_quiltBoard = board;
	}
	
	/**
	 * Checks whether or not the player has the money to purchase a piece
	 * 
	 * @param piece : the you want to buy
	 * @return boolean
	 */
	public boolean canBuyPiece(Piece piece) {
		Objects.requireNonNull(piece);
		return _buttonsCount >= piece.getCost();
	}

	/**
	 * Add the wage to the current money of the player
	 */
	public void earnWage() {
		_buttonsCount += _wage;
	}

	/**
	 * Add the given positive number to the number of buttons of the
	 * player.
	 *  
	 * @param nbButtons : the number that will be add to the number of buttons
	 */
	public void earnButtons(int nbButtons) {
		if (nbButtons < 0) {
			throw new IllegalArgumentException("The player must gain a positive amount of buttons");
		}
		_buttonsCount += nbButtons;
	}
	
	/**
	 * Set the filed _specialTile to True. 
	 */
	public void addSpecialTile() {
		_specialTile = true;
	}

	/**
	 * Add the given piece p to the quiltboard of this player
	 * 
	 * @param p : the piece you want to add
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @return success of the action
	 */
	public boolean addPieceToGrid(Piece p, int x, int y) {
		Objects.requireNonNull(p);
		return _quiltBoard.addPiece(p, x, y);
	}

	/**
	 * Handles the action skip of the player during buying phase.
	 * 
	 * @param scanner : here in case you land on a box with a patch in it
	 * @param nbMoves : number of moves
	 * @param timeBoard : the timeboard
	 * @param version : given version of the game
	 */
	public void skipTurn(Scanner scanner, int nbMoves, TimeBoard timeBoard, Constants version) {
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(version);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}

		earnButtons(nbMoves);
		move(scanner, nbMoves, timeBoard, version);
	}
	
	/**
	 * Handles the action skip of the player during buying phase.
	 * return null if the player didn't walked on a patch the patch otherwise
	 * @param nbMoves
	 * @param timeBoard
	 * @return Piece
	 */
	public Piece skipTurn(int nbMoves, TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}
		var initial = _position;
		var patch = move(nbMoves, timeBoard);
		if (timeBoard.isDoubleBox(_position)) {
			earnButtons((_position - initial)*2);
		} else {
			earnButtons(_position - initial);
		}
		return patch;
	}

	/**
	 * Pays the player with its wage.
	 */
	public void payEvent() {
		earnButtons(_wage);
	}
	
	/**
	 * The method adds the special tile to the player if can have it
	 * @return boolean
	 */
	public boolean updateSpeTile() {
 		if (_quiltBoard.checkSpecialTile()) {
 			addSpecialTile();
 			return true;
 		}
 		return false;
 	}
	
	/**
	 * The function buys a piece and and moves the player
	 * @param piece the piece recovered
	 * @param timeBoard the game's board
	 * @return a null or a patch waiting to be placed 
	 */
	public Piece recoverPiece(Piece piece, TimeBoard timeBoard) {
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(piece);
		_buttonsCount -= piece.getCost();
		_wage += piece.getButtons();
		return  move(piece.getMoves(), timeBoard);
	}

	/**
	 * Handles all the required changes needed when the player buys a piece
	 * like its money, position, quiltboard.
	 * 
	 * @param piece : piece you want to buy
	 * @param scanner : scanner for the placing phase
	 * @param timeBoard : the timeboard
	 * @param version : given version of the game
	 */
	public void buyPiece(Piece piece, Scanner scanner, TimeBoard timeBoard, Constants version) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(version);
		
		if (!canBuyPiece(piece)) {
			return;
		}
		
		if (version == Constants.PHASE1) {
			automaticPlacing(piece, scanner, version);
		} else {
			placingPhase(piece, scanner, version);
		}
		_buttonsCount -= piece.getCost();
		_wage += piece.getButtons();
		move(scanner, piece.getMoves(), timeBoard, version);
	}

	@Override
	protected void onDraw(Graphics2D graphics) {
			var text = "SPECIAL TILE";
			graphics.drawString(_name, topLeftX, topLeftY);
			graphics.drawString("You still have " + _buttonsCount + " buttons", topLeftX, topLeftY+15);
			if (_specialTile) {
				graphics.setColor(Color.RED);
				graphics.drawString(text, (topLeftX + width)/2 - (text.length()/2), topLeftY+15);
			}
		_quiltBoard.SetGraphicalProperties(0, topLeftY, width, height);
		_quiltBoard.draw(graphics);
	}
	
	/**
	 * Handles the movements of a player and especially triggers all
	 * the event that the player encounters. 
	 * 
	 * @param scanner : here in case you land on a box with a patch in it
	 * @param nbMoves : number of moves
	 * @param timeBoard : the timeboard
	 * @param version : given version of the game
	 */
	private void move(Scanner scanner, int nbMoves, TimeBoard timeBoard, Constants version) {
		Box currentBox;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			_position++;
			currentBox = timeBoard.getBoard().get(_position);
			currentBox.boxEvent(this, scanner, version);
		}
		timeBoard.getBoard().get(_position).add(this);
	}
	
	/**
	 * the function return a patch if the player is walking on one
	 * @param currentBox
	 * @return
	 */
	private Piece updatePatch(Box currentBox) {
		switch (currentBox.getStatus()) {
		case BUTTON:
			payEvent();
			return null;
		case PATCH:
			var patch = new Piece();
			patch.parseLine("1:0:0:0");
			currentBox.emptyStatus();
			return patch;
		default:
			return null;
		}
	}
	
	/**
	 * The function moves the player
	 * @param nbMoves indicate the number of moves
	 * @param timeBoard 
	 * @return a patch if the player walked in one
	 */
	private Piece move(int nbMoves, TimeBoard timeBoard) {
		Box currentBox;
		Piece patch = null;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			_position++;
			currentBox = timeBoard.getBoard().get(_position);
			patch = updatePatch(currentBox);
			if (currentBox.freeze()) {
				break;
			}
		}
		timeBoard.getBoard().get(_position).add(this);
		return patch;
	}

	// Methods - Ascii version
	
	/**
	 * the method returns the constant corresponding to the string given in parameter
	 * @param String
	 * @return Constants
	 */
	private Constants choice(String s) {
		return switch(s) {
		case "1" -> Constants.ONE;
		case "2" -> Constants.TWO;
		case "3" -> Constants.THREE;
		case ("0") -> Constants.SKIP;
		default -> Constants.DEFAULT;
		};
	}
	
	/**
	 * The method returns if the choice taken is o buy a piece
	 * @param choice
	 * @return
	 */
	private boolean buy(Constants choice) {
		return choice == Constants.ONE || choice == Constants.TWO || choice == Constants.THREE;
	}
	
	/**
	 * Handles the buying phase
	 * 
	 * @param scanner : a scanner
	 * @param p : the pieceHandler containing all the pieces
	 * @return choice of the player
	 */
	public Constants buyingPhase(Scanner scanner, PieceHandler p) {
		Objects.requireNonNull(p);
		Objects.requireNonNull(scanner);
		Constants playerChoice;
		String s;
		while (true) {
			System.out.println(_name + "'s turn :\n" + "You currently have " + _buttonsCount + " buttons \n"
					+ "Enter 1, 2 or 3 to select the according piece or enter 0 if you don't want to buy any pieces");
			s = scanner.next();
			playerChoice = choice(s);
			if (playerChoice == Constants.SKIP) {
				return playerChoice;
			}
			if (buy(playerChoice)) {
				if (canBuyPiece(p.getPiece(Integer.parseInt(s) - 1))) {
					return playerChoice;
				}
			}
		}
	}
	
	/**
	 * Automatically places the given piece if the player wants to or launch the 
	 * placing phase with the current version of the game.
	 * 
	 * @param piece : the piece to place
	 * @param scanner : the scanner
	 * @param version : given version of the game
	 */
	public void automaticPlacing(Piece piece, Scanner scanner, Constants version) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(version);
		Objects.requireNonNull(scanner);
		String userInput;
		do {
			System.out.println("Do you want to place your piece automaticly y/n");
			userInput = scanner.next();
		} while(userInput.equals("y") && userInput.equals("n"));
		if (userInput.equals("y")) {
			_quiltBoard.addPieceAutomatically(piece);
			_quiltBoard.display();
		} else {
			placingPhase(piece, scanner, version);
		}
	}

	/**
	 * Asks the player he wants to rotate/ reverse its piece and does
	 * it if he wants to.
	 * 
	 * @param scan : the scanner
	 * @param p : the given piece
	 * @return the rotated, reversed or untouched piece
	 */
	private Piece flipPiece(Scanner scan, Piece p) {
		String res;
		do {
			System.out.println("Do you want to flip the piece");
			System.out.println("Enter f if you want  to rotate it counter clockwise, r if you want to reverse it and s if you want to stop");
			res = scan.next();
			switch (res) {
			case "f" -> p = p.flip();
			case "r" -> p = p.reverse();
			}
			System.out.println(p.bodyString());
		} while (!res.equals("s"));
		return p;
	}
	
	/**
	 * Handles the placing phase.
	 * 
	 * @param piece : the piece the player wants to place
	 * @param scanner : the given scanner
	 * @param version : given version of the game
	 */
	public void placingPhase(Piece piece, Scanner scanner, Constants version) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(version);
		int x, y;
		_quiltBoard.display();
		System.out.println(piece.bodyString());
		if (version == Constants.PHASE2) {
			piece = flipPiece(scanner, piece);
		}
		do {
			try {
				System.out.println("Enter the x coordinate of the top right corner of your piece in the quiltboard");
				x = Integer.parseInt(scanner.next());
				System.out.println("Enter the y coordinate of the top right corner of your piece in the quiltboard");
				y = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				x = -1;
				y = -1;
			}
		} while (!_quiltBoard.addPiece(piece, x - 1, y - 1));
		_quiltBoard.display();
	}
	
	/**
	 * The function returns the letter corresponding to the pawn's color
	 * @return a letter
	 */
	public String display() {
		return _pawn.toString();
	}
	
	@Override
	public String toString() {
		var builder= new StringBuilder();
		builder.append(_name);
		builder.append("buttons").append(_buttonsCount).append("\n");
		builder.append("Wage").append(_wage).append("\n");
		return builder.toString();
	}
}
