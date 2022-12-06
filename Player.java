package fr.uge.patchwork;

import java.util.Objects;
import java.util.Scanner;

/**
 * This class stores the information about the Player. It also handles 
 * all its action during the playing phase. Like moving, buying a piece etc...
 * 
 * @author COUSSON Sophie
 * @author FRAIZE Victor
 */
public class Player {
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
	 * Getter for the name of the player
	 * 
	 * @return name of the player
	 */
	public String getName() {
		return _name;
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
	public void skipTurn(Scanner scanner, int nbMoves, TimeBoard timeBoard, String version) {
		Objects.requireNonNull(timeBoard);
		if (nbMoves < 0) {
			throw new IllegalArgumentException("the player can't move back");
		}

		earnButtons(nbMoves);
		move(scanner, nbMoves, timeBoard, version);
	}

	/**
	 * Pays the player with its wage.
	 */
	public void payEvent() {
		earnButtons(_wage);
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
	public void buyPiece(Piece piece, Scanner scanner, TimeBoard timeBoard, String version) {
		Objects.requireNonNull(piece);
		Objects.requireNonNull(scanner);
		Objects.requireNonNull(timeBoard);
		Objects.requireNonNull(version);
		
		if (!canBuyPiece(piece)) {
			return;
		}
		
		if (version.equals("d")) {
			automaticPlacing(piece, scanner, version);
		} else {
			placingPhase(piece, scanner, version);
		}
		_buttonsCount -= piece.getCost();
		_wage += piece.getButtons();
		move(scanner, piece.getMoves(), timeBoard, version);
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
	private void move(Scanner scanner, int nbMoves, TimeBoard timeBoard, String version) {
		Box currentBox;
		timeBoard.getBoard().get(_position).remove(this);
		for (int i = 0; i < nbMoves && _position < timeBoard.getBoard().size() - 1; i++) {
			_position++;
			currentBox = timeBoard.getBoard().get(_position);
			currentBox.boxEvent(this, scanner, version);
		}
		timeBoard.getBoard().get(_position).add(this);
	}

	// Methods - Ascii version
	/**
	 * Handles the buying phase
	 * 
	 * @param scanner : a scanner
	 * @param p : the pieceHandler containing all the pieces
	 * @return choice of the player
	 */
	public int buyingPhase(Scanner scanner, PieceHandler p) {
		int playerChoice;
		do {
			try {
				System.out.println(_name + "'s turn :\n" + "You currently have " + _buttonsCount + " buttons \n"
						+ "Enter 1, 2 or 3 to select the according piece or enter 0 if you don't want to buy any pieces");
				playerChoice = Integer.parseInt(scanner.next());
			} catch (NumberFormatException e) {
				playerChoice = -1;
			}
			if (playerChoice == 0) {
				return playerChoice;
			}
		} while (playerChoice < 1 || playerChoice > 3 || !canBuyPiece(p.getPiece(playerChoice - 1)));

		return playerChoice;
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
	 * Automatically places the given piece if the player wants to or launch the 
	 * placing phase with the current version of the game.
	 * 
	 * @param piece : the piece to place
	 * @param scanner : the scanner
	 * @param version : given version of the game
	 */
	public void automaticPlacing(Piece piece, Scanner scanner, String version) {
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
	 * Handles the placing phase.
	 * 
	 * @param piece : the piece the player wants to place
	 * @param scanner : the given scanner
	 * @param version : given version of the game
	 */
	public void placingPhase(Piece piece, Scanner scanner, String version) {
		int x, y;
		_quiltBoard.display();
		System.out.println(piece.bodyString());
		if (version.equals("a")) {
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

	@Override
	public String toString() {
		var builder= new StringBuilder();
		builder.append(_name);
		builder.append("buttons").append(_buttonsCount).append("\n");
		builder.append("Wage").append(_wage).append("\n");
		return builder.toString();
	}
}
